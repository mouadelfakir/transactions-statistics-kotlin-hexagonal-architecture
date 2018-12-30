package com.n26.transactionstatistics.statistics

import com.n26.transactionstatistics.statistics.domain.Element
import com.n26.transactionstatistics.statistics.domain.Statistics
import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.math.RoundingMode
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StatisticsStoreTest {

    private lateinit var statisticsStore: StatisticsStore

    @Before
    fun setUp() {
        statisticsStore = StatisticsStore(Executors.newSingleThreadScheduledExecutor())
    }

    @Test
    fun `can add element to store`() {
        val element = Element(statisticsStore, valueOf(1234), Instant.now())
        assertTrue(statisticsStore.add(element))
    }

    @Test
    fun `expired elements get evicted`() {
        val element = Element(statisticsStore, valueOf(1234), Instant.now().minusSeconds(60))

        statisticsStore.add(element)
        assertTrue(statisticsStore.has(element))

        TimeUnit.SECONDS.sleep(1)

        assertFalse(statisticsStore.has(element))
    }

    @Test
    fun `can eject many expired elements`() {
        val element1 = Element(statisticsStore, valueOf(1234), Instant.now().minusSeconds(59))
        val element2 = Element(statisticsStore, valueOf(1234), Instant.now().minusSeconds(57))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertTrue(statisticsStore.has(element1))
        assertTrue(statisticsStore.has(element2))

        TimeUnit.SECONDS.sleep(2)

        assertFalse(statisticsStore.has(element1))
        assertTrue(statisticsStore.has(element2))

        TimeUnit.SECONDS.sleep(2)

        assertFalse(statisticsStore.has(element1))
        assertFalse(statisticsStore.has(element2))

    }

    @Test
    fun `can clear all statistics store`() {
        val element1 = Element(statisticsStore, valueOf(1.005), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9.34), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        statisticsStore.clear()

        assertFalse(statisticsStore.has(element1))
        assertFalse(statisticsStore.has(element2))
    }

    @Test
    fun `can calculate statistics sum after add`() {
        val element1 = Element(statisticsStore, valueOf(1.005), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9.34), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(10.35),  statisticsStore.sum());
    }

    @Test
    fun `can calculate statistics sum after evict`() {
        val element1 = Element(statisticsStore, valueOf(1.005), Instant.now().minusSeconds(59))
        val element2 = Element(statisticsStore, valueOf(9.34), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(10.35),  statisticsStore.sum());

        TimeUnit.SECONDS.sleep(2)

        assertEquals(valueOf(9.34),  statisticsStore.sum());
    }

    @Test
    fun `sum should be zero when statistics store get cleared`() {
        val element1 = Element(statisticsStore, valueOf(1.005), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9.34), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(10.35),  statisticsStore.sum());

        statisticsStore.clear()

        assertEquals(valueOf(0.00).setScale(2, RoundingMode.HALF_UP),  statisticsStore.sum());
    }

    @Test
    fun `should return the right count when elements added`() {
        val element1 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(2,  statisticsStore.count());
    }

    @Test
    fun `should return the right count when elements evicted`() {
        val element1 = Element(statisticsStore, valueOf(1.005), Instant.now().minusSeconds(60))
        val element2 = Element(statisticsStore, valueOf(9.34), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(2,  statisticsStore.count());

        TimeUnit.SECONDS.sleep(1)

        assertEquals(1,  statisticsStore.count())
    }

    @Test
    fun `should return count zero when store get cleared`() {
        val element1 = Element(statisticsStore, valueOf(1.005), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9.34), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(2,  statisticsStore.count());

        statisticsStore.clear()

        assertEquals(0,  statisticsStore.count())
    }

    @Test
    fun `should return the right avg when elements are added`() {
        val element1 = Element(statisticsStore, valueOf(10), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(11), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(10.50).setScale(2, RoundingMode.HALF_UP),  statisticsStore.avg());
    }

    @Test
    fun `should return the right avg when elements are evicted`() {
        val element1 = Element(statisticsStore, valueOf(10), Instant.now().minusSeconds(60))
        val element2 = Element(statisticsStore, valueOf(11), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(10.50).setScale(2, RoundingMode.HALF_UP),  statisticsStore.avg());

        TimeUnit.SECONDS.sleep(1)

        assertEquals(valueOf(11).setScale(2, RoundingMode.HALF_UP),  statisticsStore.avg());
    }

    @Test
    fun `avg should be zero when store get cleared`() {
        val element1 = Element(statisticsStore, valueOf(10), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(11), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(10.50).setScale(2, RoundingMode.HALF_UP),  statisticsStore.avg())

        statisticsStore.clear()

        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.avg())
    }

    @Test
    fun `should return the right minimum amount when elements are added`() {
        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(9).setScale(2, RoundingMode.HALF_UP), statisticsStore.min())
    }

    @Test
    fun `should return the right minimum when elements evicted`() {
        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(57))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(59))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(9).setScale(2, RoundingMode.HALF_UP), statisticsStore.min())

        TimeUnit.SECONDS.sleep(2)

        assertEquals(valueOf(12).setScale(2, RoundingMode.HALF_UP), statisticsStore.min())

        TimeUnit.SECONDS.sleep(2)

        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP), statisticsStore.min())
    }

    @Test
    fun `minimum should be zero when store get cleared`() {
        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(9).setScale(2, RoundingMode.HALF_UP),  statisticsStore.min())

        statisticsStore.clear()

        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.min())
    }

    @Test
    fun `should return the right maximum amount when elements are added`() {
        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(12).setScale(2, RoundingMode.HALF_UP), statisticsStore.max())
    }

    @Test
    fun `should return the right maximum when elements evicted`() {
        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(58))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(59))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(12).setScale(2, RoundingMode.HALF_UP), statisticsStore.max())

        TimeUnit.SECONDS.sleep(1)

        assertEquals(valueOf(12).setScale(2, RoundingMode.HALF_UP), statisticsStore.max())

        TimeUnit.SECONDS.sleep(2)

        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP), statisticsStore.max())
    }

    @Test
    fun `maximum should be zero when store get cleared`() {
        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)

        assertEquals(valueOf(12.0).setScale(2, RoundingMode.HALF_UP),  statisticsStore.max())

        statisticsStore.clear()

        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.max())
    }

    @Test
    fun `empty store should return zero sum, avg, min, max and count`() {
        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.sum())
        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.avg())
        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.min())
        assertEquals(ZERO.setScale(2, RoundingMode.HALF_UP),  statisticsStore.max())
        assertEquals(0,  statisticsStore.count())
    }

    @Test
    fun `load statistics`() {

        val element1 = Element(statisticsStore, valueOf(12), Instant.now().minusSeconds(10))
        val element2 = Element(statisticsStore, valueOf(9.11), Instant.now().minusSeconds(10))
        val element3 = Element(statisticsStore, valueOf(17.12), Instant.now().minusSeconds(10))

        statisticsStore.add(element1)
        statisticsStore.add(element2)
        statisticsStore.add(element3)

        val statistics : Statistics = statisticsStore.statistics()

        assertEquals(valueOf(38.23), statistics.sum)
        assertEquals(valueOf(12.74), statistics.avg)
        assertEquals(valueOf(17.12), statistics.max)
        assertEquals(valueOf(9.11), statistics.min)
        assertEquals(3, statistics.count)

    }
}