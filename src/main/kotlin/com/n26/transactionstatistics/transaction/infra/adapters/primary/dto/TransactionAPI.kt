package com.n26.transactionstatistics.transaction.infra.adapters.primary.dto

import com.n26.transactionstatistics.transaction.domain.DomainException
import com.n26.transactionstatistics.transaction.domain.Transaction
import com.n26.transactionstatistics.transaction.infra.adapters.InfraException
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import java.time.Instant
import java.time.format.DateTimeParseException

data class TransactionAPI(val amount: String, val timestamp: String) {

    fun toTransaction() : Transaction {
        try {
            return Transaction(amount.toBigDecimal(), Instant.parse(timestamp))

        } catch (e : Exception) {
            when(e) {
                is NumberFormatException -> throw InfraException(UNPROCESSABLE_ENTITY.value())
                is DateTimeParseException -> throw InfraException(UNPROCESSABLE_ENTITY.value())
                is DomainException -> throw InfraException(e.code)
                else -> throw e
            }
        }
    }

}
