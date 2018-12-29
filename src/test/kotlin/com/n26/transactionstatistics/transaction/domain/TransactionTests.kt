package com.n26.transactionstatistics.transaction.domain

import com.n26.transactionstatistics.transaction.domain.TransactionError.IN_FUTURE_ERR
import com.n26.transactionstatistics.transaction.domain.TransactionError.OLDER_THAN_60S_ERR
import org.junit.Test
import java.lang.RuntimeException
import java.math.BigDecimal.valueOf
import java.time.Instant
import kotlin.test.assertEquals

class TransactionTests {

    @Test
    fun can_create_transaction_successfully_test() {

        val now = Instant.now()

        val transaction = Transaction(valueOf(1234), now)

        assertEquals(valueOf(1234), transaction.amount)
        assertEquals(now, transaction.timestamp)
    }

    @Test(expected = TransactionCreationException::class)
    fun can_not_create_transaction_when_timestamp_in_future() {
        try {
            Transaction(valueOf(1233), Instant.now().plusSeconds(10))
        } catch (exp: RuntimeException) {
            when (exp){
                is TransactionCreationException -> {
                    assertEquals(exp.code, IN_FUTURE_ERR)
                    throw exp
                }
            }
        }
    }

    @Test(expected = TransactionCreationException::class)
    fun can_not_create_transaction_when_timestamp_is_older_than_60s() {
        try {
            Transaction(valueOf(1233), Instant.now().minusSeconds(61))
        } catch (exp: RuntimeException) {
            when (exp){
                is TransactionCreationException -> {
                    assertEquals(exp.code, OLDER_THAN_60S_ERR)
                    throw exp
                }
            }
        }
    }
}