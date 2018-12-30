package com.n26.transactionstatistics.statistics.domain

import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import com.n26.transactionstatistics.util.kPlusSeconds
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit.SECONDS
import java.util.concurrent.Callable

data class Element(val context: StatisticsStore, val amount: BigDecimal, val timestamp: Instant) : Callable<Unit>{

    init {
        val delay = SECONDS.between(Instant.now(), timestamp.kPlusSeconds())
        context.scheduleEvict(this, delay)
    }

    override fun call() {
        context.evict(this)
    }
}
