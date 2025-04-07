package com.bancx.loan.repositories;

import com.bancx.loan.entities.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(LoanRepository repository) {

        return args -> {
            log.info("Preloading {}", repository.save(new Loan(new BigDecimal("25000.00"), 3)));
            log.info("Preloading {}", repository.save(new Loan(new BigDecimal("256000.75"), 60)));
        };
    }
}
