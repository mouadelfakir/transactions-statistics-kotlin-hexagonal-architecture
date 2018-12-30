package com.n26.transactionstatistics.transaction.infra

import com.n26.transactionstatistics.transaction.domain.Transaction
import com.n26.transactionstatistics.transaction.domain.ports.secondary.TransactionRecords
import com.n26.transactionstatistics.transaction.infra.adapters.secondary.InMemoryTransactionRecords
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals

class InMemoryTransactionRecordsTest {

    private lateinit var transactionRecords: TransactionRecords

    @Before
    fun setUp() {
        transactionRecords = InMemoryTransactionRecords()
    }

    @Test
    fun `can persist a transaction`() {
        val transaction = Transaction(BigDecimal.valueOf(1234), Instant.now())
        assertEquals(transaction, transactionRecords.add(transaction))
    }

    @Test
    fun `can add many transactions`() {
        val transaction1 = Transaction(BigDecimal.valueOf(1234), Instant.now())
        val transaction2 = Transaction(BigDecimal.valueOf(12345), Instant.now())


        transactionRecords.add(transaction1)
        transactionRecords.add(transaction2)

        val transactions = transactionRecords.getAll()
        assertEquals(2, transactions.size)
        assertEquals(transaction1, transactions[0])
        assertEquals(transaction2, transactions[1])
    }

    @Test
    fun `can remove all transactions`() {
        val transaction1 = Transaction(BigDecimal.valueOf(1234), Instant.now())
        val transaction2 = Transaction(BigDecimal.valueOf(12345), Instant.now())

        transactionRecords.add(transaction1)
        transactionRecords.add(transaction2)
        transactionRecords.removeAll()

        assertEquals(0, transactionRecords.getAll().size)
    }
}