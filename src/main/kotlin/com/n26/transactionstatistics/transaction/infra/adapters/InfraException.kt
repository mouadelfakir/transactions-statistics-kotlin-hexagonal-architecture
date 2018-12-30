package com.n26.transactionstatistics.transaction.infra.adapters

import com.n26.transactionstatistics.transaction.domain.TransactionError
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class InfraException(val code: Int) : RuntimeException() {

    constructor(transactionError: TransactionError) : this(errorCodeFor(transactionError))
}


fun errorCodeFor(transactionError: TransactionError) : Int {

    return when(transactionError) {
        TransactionError.OLDER_THAN_60S_ERR -> HttpStatus.NO_CONTENT.value()
        TransactionError.IN_FUTURE_ERR -> HttpStatus.UNPROCESSABLE_ENTITY.value()
    }
}