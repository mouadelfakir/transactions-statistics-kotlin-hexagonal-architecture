package com.n26.transactionstatistics.transaction.infra

import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import com.n26.transactionstatistics.transaction.infra.adapters.primary.TransactionsEdgeAPI
import com.n26.transactionstatistics.transaction.infra.adapters.primary.dto.TransactionAPI
import com.n26.transactionstatistics.transaction.infra.adapters.secondary.InMemoryTransactionRecords
import org.junit.Before
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TransactionsEdgeAPITest {

    private lateinit var transactionsEdgeAPI: TransactionsEdgeAPI

    private val transactionRecords = InMemoryTransactionRecords()

    @Before
    fun setUp() {
        transactionsEdgeAPI = TransactionsEdgeAPI(TransactionsManager(transactionRecords));
    }

    @Test
    fun `return error code 422 when create transaction with amount not parsable`() {
        val responseEntity = transactionsEdgeAPI.add(TransactionAPI("ee123", Instant.now().toString()))
        assertEquals(422, responseEntity.statusCode.value())
    }

    @Test
    fun `return error code 422 when create transaction with timestamp not parsable`() {

        val response = transactionsEdgeAPI.add(TransactionAPI("1234", "123-ee-11"))
        assertEquals(422, response.statusCode.value())
    }

    @Test
    fun `return error code 204 when transaction is older than 60s`() {

        val response = transactionsEdgeAPI.add(TransactionAPI("1234", Instant.now().minusSeconds(61).toString()))
        assertEquals(204, response.statusCode.value())
    }


    @Test
    fun `return error code 422 when transaction is in the future`() {
        val response = transactionsEdgeAPI.add(TransactionAPI("1234", Instant.now().plusSeconds(10).toString()))
        assertEquals(422, response.statusCode.value())
    }

    @Test
    fun `return 201 when received transaction has no error`() {
        val response = transactionsEdgeAPI.add(TransactionAPI("1234", Instant.now().toString()))

        assertEquals(201, response.statusCode.value())
        assertEquals(1, transactionRecords.getAll().size)
    }

    @Test
    fun `can remove all transactions`() {
        transactionsEdgeAPI.add(TransactionAPI("1111", Instant.now().toString()))
        transactionsEdgeAPI.add(TransactionAPI("2222", Instant.now().toString()))

        val response = transactionsEdgeAPI.removeAll();

        assertEquals(204, response.statusCode.value())
        assertTrue(transactionRecords.getAll().isEmpty());
    }
}