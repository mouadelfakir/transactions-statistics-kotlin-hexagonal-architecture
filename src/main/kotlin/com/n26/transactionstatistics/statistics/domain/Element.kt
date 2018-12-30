package com.n26.transactionstatistics.statistics.domain

import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.Callable

data class Element(val statisticsStore: StatisticsStore, val amount: BigDecimal, val timestamp: Instant) : Callable<Unit>{


    init {
        val delay = ChronoUnit.SECONDS.between(Instant.now(), timestamp.plusSeconds(60))
        statisticsStore.scheduleEvict(this, delay)
    }

    override fun call() {
        statisticsStore.evict(this)
    }
}
