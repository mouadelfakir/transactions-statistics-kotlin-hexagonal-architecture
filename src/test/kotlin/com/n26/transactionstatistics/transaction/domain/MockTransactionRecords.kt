package com.n26.transactionstatistics.transaction.domain

class MockTransactionRecords : TransactionRecords {

    var removeAllCalls = 0

    override fun add(transaction: Transaction) : Transaction = transaction.copy(transaction.amount, transaction.timestamp)

    override fun removeAll() {
        removeAllCalls++
    }
}
