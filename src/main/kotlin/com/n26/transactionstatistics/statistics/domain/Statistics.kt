package com.n26.transactionstatistics.statistics.domain

import java.math.BigDecimal

data class Statistics(val sum: BigDecimal, val avg: BigDecimal, val max: BigDecimal, val min: BigDecimal, val count: Long)