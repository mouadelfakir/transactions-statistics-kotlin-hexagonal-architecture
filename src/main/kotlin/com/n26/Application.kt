package com.n26

import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import com.n26.transactionstatistics.statistics.infra.adapters.secondary.StatisticsEventHandler
import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import com.n26.transactionstatistics.transaction.infra.adapters.secondary.DummyTransactionRecords
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.util.concurrent.Executors

@SpringBootApplication
open class Application {

    @Bean
    open fun transactionsManager(): TransactionsManager = TransactionsManager(DummyTransactionRecords())

    @Bean
    open fun statisticsStore(): StatisticsStore = StatisticsStore(Executors.newScheduledThreadPool(2))

    @Bean
    open fun statisticsEventHandler(): StatisticsEventHandler {
        return StatisticsEventHandler(statisticsStore())
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)

}



