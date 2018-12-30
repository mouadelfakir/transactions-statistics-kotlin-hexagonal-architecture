package com.n26.transactionstatistics.statistics.infra.adapters.primary

import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import com.n26.transactionstatistics.statistics.infra.adapters.primary.dto.StatisticsAPI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/statistics")
class StatisticsEdgeAPI(private val statisticsStore: StatisticsStore) {

    @GetMapping
    fun get() : ResponseEntity<StatisticsAPI> {
        val statistics = statisticsStore.statistics()
        return ResponseEntity.ok(StatisticsAPI(
                statistics.sum.toString(),
                statistics.avg.toString(),
                statistics.max.toString(),
                statistics.min.toString(),
                statistics.count
        ))
    }
}