package com.n26.transactionstatistics.transaction.domain.ports.secondary

import com.n26.transactionstatistics.transaction.domain.Transaction

interface TransactionRecords {

    fun add(transaction: Transaction) : Transaction
    fun removeAll()
    fun getAll(): List<Transaction>

}
