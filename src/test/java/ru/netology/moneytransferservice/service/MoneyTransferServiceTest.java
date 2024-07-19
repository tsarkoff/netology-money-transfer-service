package ru.netology.moneytransferservice.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.netology.moneytransferservice.repository.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTransferServiceTest {
    private static int operationId = -1;

    @MockBean
    CardRepositoryImpl cardRepository;

    @Autowired
    MoneyTransferService moneyTransferService;

    @Test
    @Order(1)
    void doTransfer() {
        Card cardFrom = new Card("3333000033330000", "03/25", "333");
        Card cardTo = new Card("4444000044440000", "04/25", "444");
        String cardToNumber = "4444000044440000";
        when(cardRepository.getCard(cardFrom)).thenReturn(cardFrom);
        when(cardRepository.getCardByNumber(cardToNumber)).thenReturn(cardTo);
        when(cardRepository.saveOperationId(anyInt())).thenReturn(true);

        TransferRequestDto transferRequestDto = new TransferRequestDto(
                cardFrom,
                "4444000044440000",
                new Amount(100_000, Currency.RUR)
        );

        OperationDto actualOperationDto = moneyTransferService.doTransfer(transferRequestDto);
        operationId = moneyTransferService.getOperationId();
        OperationDto expectedOperationDto = new OperationDto(operationId);
        assertEquals(expectedOperationDto, actualOperationDto);
    }

    @Test
    @Order(2)
    void doConfirm() {
        ConfirmRequestDto confirmRequestDto = new ConfirmRequestDto(operationId, "0000");
        when(cardRepository.confirmOperationId(anyInt())).thenReturn(true);
        OperationDto actualOperationDto = moneyTransferService.doConfirm(confirmRequestDto);
        OperationDto expectedOperationDto = new OperationDto(operationId);
        assertEquals(expectedOperationDto, actualOperationDto);
    }
}