package com.n26.transactionstatistics.transaction.infra.adapters

import com.n26.transactionstatistics.transaction.domain.TransactionError
import com.n26.transactionstatistics.transaction.domain.TransactionError.IN_FUTURE_ERR
import com.n26.transactionstatistics.transaction.domain.TransactionError.OLDER_THAN_60S_ERR
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class InfraException(val code: Int) : RuntimeException() {

    constructor(transactionError: TransactionError) : this(mapping(transactionError))
}


fun mapping(transactionError: TransactionError) : Int {

    return when(transactionError) {
        OLDER_THAN_60S_ERR -> NO_CONTENT.value()
        IN_FUTURE_ERR -> UNPROCESSABLE_ENTITY.value()
    }
}