package com.n26.transactionstatistics.transaction.domain

class TransactionsManager(val transactionRecords: TransactionRecords) {

    fun add(transaction: Transaction) : Transaction {
        return transactionRecords.add(transaction)
    }

    fun removeAll() {
        transactionRecords.removeAll()
    }

}
