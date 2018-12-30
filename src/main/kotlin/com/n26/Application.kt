package com.n26

import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager
import com.n26.transactionstatistics.transaction.infra.adapters.secondary.InMemoryTransactionRecords
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application{



    @Bean
    open fun transactionsManager(): TransactionsManager {
        return TransactionsManager(InMemoryTransactionRecords())
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}



