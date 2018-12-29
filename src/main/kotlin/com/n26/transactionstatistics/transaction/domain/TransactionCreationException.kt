package com.n26.transactionstatistics.transaction.domain

import java.lang.RuntimeException

class TransactionCreationException(val code: TransactionError) : RuntimeException()

enum class TransactionError {

    IN_FUTURE_ERR, OLDER_THAN_60S_ERR;

    fun throwIt() {
        throw TransactionCreationException(this)
    }
}
