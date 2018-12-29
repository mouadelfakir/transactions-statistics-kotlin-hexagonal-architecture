package com.n26.transactionstatistics.transaction.domain

import org.junit.Test
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals

class TransactionsManagerTest {

    @Test
    fun can_add_a_transaction_test() {
        val transaction = Transaction(BigDecimal.valueOf(1234), Instant.now())
        val transactionRecords = MockTransactionRecords()

        assertEquals(transaction, TransactionsManager(transactionRecords).add(transaction))
    }

    @Test
    fun can_delete_all_transactions_test() {
        val transactionRecords = MockTransactionRecords()
        TransactionsManager(transactionRecords).removeAll()

        assertEquals(1, transactionRecords.removeAllCalls)
    }
}