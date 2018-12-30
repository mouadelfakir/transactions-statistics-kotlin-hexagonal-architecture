package com.n26.transactionstatistics.transaction.infra.adapters.primary

import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import com.n26.transactionstatistics.transaction.infra.adapters.InfraException
import com.n26.transactionstatistics.transaction.infra.adapters.primary.dto.TransactionAPI
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions")
class TransactionsEdgeAPI(val transactionsManager : TransactionsManager) {

    @PostMapping
    fun add(@RequestBody transactionAPI: TransactionAPI) : ResponseEntity<Unit> {
        try {
            transactionsManager.add(transactionAPI.toTransaction())

        } catch (exp : InfraException) {
            return ResponseEntity.status(exp.code).build()
        }
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping
    fun removeAll() : ResponseEntity<Unit> {
        transactionsManager.removeAll()
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
