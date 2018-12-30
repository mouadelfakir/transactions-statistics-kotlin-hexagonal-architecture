package com.n26.transactionstatistics.statistics.infra.adapters.secondary

import com.n26.transactionstatistics.statistics.domain.Element
import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import com.n26.transactionstatistics.event.TransactionCreatedEvent
import com.n26.transactionstatistics.event.TransactionsClearedEvent
import org.springframework.context.event.EventListener

open class StatisticsEventHandler(private val statisticsStore: StatisticsStore) {

    @EventListener
    open fun handle(event: TransactionCreatedEvent){
        statisticsStore.add(Element(statisticsStore, event.amount, event.timestamp))
    }

    @EventListener
    open fun handle(event: TransactionsClearedEvent){
        statisticsStore.clear()
    }
}