package com.n26.transactionstatistics.transaction.infra.adapters.secondary

import com.n26.transactionstatistics.transaction.domain.Transaction
import com.n26.transactionstatistics.transaction.domain.ports.secondary.TransactionRecords

class DummyTransactionRecords : TransactionRecords {
    override fun add(transaction: Transaction): Transaction {
        // DO NOTHING
        return transaction
    }

    override fun removeAll() {
        // DO NOTHING
    }

    override fun getAll(): List<Transaction> {
        return emptyList()
    }
}