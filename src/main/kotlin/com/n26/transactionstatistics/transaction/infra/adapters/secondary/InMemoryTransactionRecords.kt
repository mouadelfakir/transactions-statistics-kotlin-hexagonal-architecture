package com.n26.transactionstatistics.transaction.infra.adapters.secondary

import com.n26.transactionstatistics.transaction.domain.Transaction
import com.n26.transactionstatistics.transaction.domain.ports.secondary.TransactionRecords

class InMemoryTransactionRecords : TransactionRecords {

    val transactions = ArrayList<Transaction>()

    override fun add(transaction: Transaction): Transaction {
        transactions.add(transaction)
        return transaction
    }

    override fun removeAll() {
        transactions.clear()
    }

    override fun getAll(): List<Transaction> = transactions
}
