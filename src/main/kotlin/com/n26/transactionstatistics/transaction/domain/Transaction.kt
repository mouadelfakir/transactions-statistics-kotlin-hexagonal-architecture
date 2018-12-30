package com.n26.transactionstatistics.transaction.domain

import com.n26.transactionstatistics.transaction.domain.TransactionError.IN_FUTURE_ERR
import com.n26.transactionstatistics.transaction.domain.TransactionError.OLDER_THAN_60S_ERR
import com.n26.transactionstatistics.util.n26MinusSeconds
import java.math.BigDecimal
import java.time.Instant

data class Transaction(val amount: BigDecimal, val timestamp: Instant) {

    init {
        if(Instant.now().isBefore(timestamp))
            IN_FUTURE_ERR.throwIt()

        if(Instant.now().n26MinusSeconds().isAfter(timestamp))
            OLDER_THAN_60S_ERR.throwIt()
    }
}
