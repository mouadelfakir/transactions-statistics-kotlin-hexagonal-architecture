package com.n26.transactionstatistics.transaction.domain.mocks

import com.n26.transactionstatistics.transaction.domain.Transaction
import com.n26.transactionstatistics.transaction.domain.ports.secondary.TransactionRecords

class MockTransactionRecords : TransactionRecords {

    var removeAllCalls = 0

    override fun add(transaction: Transaction) : Transaction = transaction.copy(transaction.amount, transaction.timestamp)

    override fun removeAll() {
        removeAllCalls++
    }

    override fun getAll(): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
