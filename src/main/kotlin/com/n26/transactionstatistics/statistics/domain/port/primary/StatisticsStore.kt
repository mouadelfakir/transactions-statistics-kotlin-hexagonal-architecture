package com.n26.transactionstatistics.statistics.domain.port.primary

import com.n26.transactionstatistics.statistics.domain.Element
import com.n26.transactionstatistics.statistics.domain.Statistics
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.math.RoundingMode
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class StatisticsStore(val executor: ScheduledExecutorService, var sum : BigDecimal = ZERO, var min : BigDecimal = ZERO, var max : BigDecimal = ZERO) {

    private val elements = ConcurrentLinkedDeque<Element>()

    @Synchronized
    fun add(element: Element): Boolean {
        beforeAdd(element)
        return elements.add(element)
    }

    @Synchronized
    fun evict(element: Element) {
        elements.remove(element)
        afterEvict(element)
    }

    @Synchronized
    fun clear() {
        elements.clear()
        afterClear()
    }

    fun scheduleEvict(element: Element, delay: Long) {
        executor.schedule(element, delay, TimeUnit.SECONDS)
    }

    fun has(element: Element): Boolean = elements.contains(element)

    private fun beforeAdd(element: Element) {
        sum = sum.add(element.amount)
        min = if (count() == 0) element.amount else if (element.amount < min) element.amount else min
        max = if (count() == 0) element.amount else if (element.amount > max) element.amount else max
    }

    private fun afterEvict(element: Element) {
        sum = sum.minus(element.amount)
        min = elements.stream().map { e -> e.amount }.min { a, b -> a.compareTo(b) }.orElse(ZERO)
        max = elements.stream().map { e -> e.amount }.max { a, b -> a.compareTo(b) }.orElse(ZERO)
    }

    private fun afterClear() {
        sum = ZERO
        min = ZERO
        max = ZERO
    }

    fun sum(): BigDecimal = sum.to2Up()

    fun avg(): BigDecimal = if (elements.isEmpty()) ZERO.to2Up() else sum.divideTo2Up(valueOf(count().toLong()))

    fun min(): BigDecimal = min.to2Up()

    fun max(): BigDecimal = max.to2Up()

    fun count(): Int = elements.size

    fun statistics(): Statistics = Statistics(sum(), avg(), max(), min(), count())

}

fun BigDecimal.to2Up() = this.setScale(2, RoundingMode.HALF_UP)
fun BigDecimal.divideTo2Up(other: BigDecimal) = this.divide(other, 2, RoundingMode.HALF_UP)