package ru.netology.moneytransferservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.moneytransferservice.repository.CardRepositoryImpl;
import ru.netology.moneytransferservice.service.MoneyTransferService;

@Configuration
public class JavaConfig {
    @Bean
    public CardRepositoryImpl cardRepository() {
        return new CardRepositoryImpl();
    }

    @Bean
    public MoneyTransferService moneyTransferService(CardRepositoryImpl cardRepository) {
        return new MoneyTransferService(cardRepository);
    }
}
