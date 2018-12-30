package com.n26

import com.n26.transactionstatistics.statistics.domain.port.primary.StatisticsStore
import com.n26.transactionstatistics.statistics.infra.adapters.secondary.StatisticsEventHandler
import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import com.n26.transactionstatistics.transaction.infra.adapters.secondary.InMemoryTransactionRecords
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executors

@EnableAsync
@SpringBootApplication
open class Application {

    @Bean
    open fun transactionsManager(): TransactionsManager = TransactionsManager(InMemoryTransactionRecords())

    @Bean
    open fun statisticsStore(): StatisticsStore = StatisticsStore(Executors.newScheduledThreadPool(2))

    @Bean
    open fun statisticsEventHandler(): StatisticsEventHandler = StatisticsEventHandler(statisticsStore())

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}



