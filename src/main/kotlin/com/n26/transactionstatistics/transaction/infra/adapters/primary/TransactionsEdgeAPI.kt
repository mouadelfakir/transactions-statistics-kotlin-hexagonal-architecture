package com.n26.transactionstatistics.transaction.infra.adapters.primary

import com.n26.transactionstatistics.event.TransactionCreatedEvent
import com.n26.transactionstatistics.event.TransactionsClearedEvent
import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import com.n26.transactionstatistics.transaction.infra.adapters.InfraException
import com.n26.transactionstatistics.transaction.infra.adapters.primary.dto.TransactionAPI
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions")
class TransactionsEdgeAPI(val transactionsManager : TransactionsManager, val applicationPublisher: ApplicationEventPublisher) {

    @PostMapping
    fun add(@RequestBody transactionAPI: TransactionAPI) : ResponseEntity<Unit> {
        try {
            val transaction = transactionAPI.toTransaction()
            transactionsManager.add(transaction)
            applicationPublisher.publishEvent(TransactionCreatedEvent(transaction.amount, transaction.timestamp))
        } catch (exp : InfraException) {
            return ResponseEntity.status(exp.code).build()
        }
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping
    fun removeAll() : ResponseEntity<Unit> {
        transactionsManager.removeAll()
        applicationPublisher.publishEvent(TransactionsClearedEvent())
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
