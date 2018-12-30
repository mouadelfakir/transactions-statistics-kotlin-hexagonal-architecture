package com.n26.transactionstatistics.transaction.domain

import java.lang.RuntimeException

open class DomainException(val code: TransactionError): RuntimeException()

class TransactionCreationException(code: TransactionError) : DomainException(code)

enum class TransactionError {

    IN_FUTURE_ERR, OLDER_THAN_60S_ERR;

    fun throwIt() {
        throw TransactionCreationException(this)
    }
}

