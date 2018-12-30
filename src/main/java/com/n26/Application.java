package com.n26;

import com.n26.transactionstatistics.transaction.domain.ports.primary.TransactionsManager;
import com.n26.transactionstatistics.transaction.infra.adapters.secondary.InMemoryTransactionRecords;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TransactionsManager transactionsManager() {
        return new TransactionsManager(new InMemoryTransactionRecords());
    }
}
