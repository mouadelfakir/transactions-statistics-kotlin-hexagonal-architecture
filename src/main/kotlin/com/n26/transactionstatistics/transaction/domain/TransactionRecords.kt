package com.n26.transactionstatistics.transaction.domain

interface TransactionRecords {

    fun add(transaction: Transaction) : Transaction
    fun removeAll()

}
