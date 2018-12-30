package com.n26.transactionstatistics.event

import java.math.BigDecimal
import java.time.Instant

class TransactionCreatedEvent(val amount: BigDecimal, val timestamp: Instant)