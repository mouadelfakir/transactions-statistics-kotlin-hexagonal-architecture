package com.n26.transactionstatistics.transaction.domain.ports.primary

import com.n26.transactionstatistics.transaction.domain.Transaction
import com.n26.transactionstatistics.transaction.domain.ports.secondary.TransactionRecords

class TransactionsManager(private val transactionRecords: TransactionRecords) {

    fun add(transaction: Transaction) : Transaction {
        return transactionRecords.add(transaction)
    }

    fun removeAll() {
        transactionRecords.removeAll()
    }

}
