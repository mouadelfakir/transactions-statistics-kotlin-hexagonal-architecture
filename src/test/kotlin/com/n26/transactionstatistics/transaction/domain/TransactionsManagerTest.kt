package com.n26.transactionstatistics.transaction.domain

import com.n26.transactionstatistics.transaction.domain.mocks.MockTransactionRecords
import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals

class TransactionsManagerTest {

    @Test
    fun `can add a transaction`() {
        val transaction = Transaction(BigDecimal.valueOf(1234), Instant.now())
        val transactionRecords = MockTransactionRecords()

        assertEquals(transaction, TransactionsManager(transactionRecords).add(transaction))
    }

    @Test
    fun `can delete all transactions`() {
        val transactionRecords = MockTransactionRecords()
        TransactionsManager(transactionRecords).removeAll()

        assertEquals(1, transactionRecords.removeAllCalls)
    }
}