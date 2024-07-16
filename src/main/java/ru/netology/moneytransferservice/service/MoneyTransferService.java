package ru.netology.moneytransferservice.service;

import lombok.AllArgsConstructor;
import ru.netology.moneytransferservice.logger.LogSeverity;
import ru.netology.moneytransferservice.logger.Logger;
import ru.netology.moneytransferservice.repository.*;

import java.util.Random;

@AllArgsConstructor
public class MoneyTransferService {
    private final CardRepository cardRepository;

    public OperationDto doTransfer(TransferRequestDto transferRequest) {
        Card cardFrom = cardRepository.getCard(transferRequest.cardFrom());
        Card cardTo = cardRepository.getCardByNumber(transferRequest.cardToNumber().replace("\"", ""));
        if (cardFrom == null) {
            Logger.getLogger().log(LogSeverity.WARN, transferRequest, null);
            throw new IllegalArgumentException("NotFoundException = cardFrom not found or wrong expiration / CVV data");
        }
        if (cardTo == null) {
            Logger.getLogger().log(LogSeverity.WARN, transferRequest, null);
            throw new IllegalArgumentException("NotFoundException = cardTo not found or wrong expiration / CVV data");
        }
        String operationId = Integer.toString(Math.abs(new Random().nextInt()));
        if (!cardRepository.saveOperationId(operationId)) {
            Logger.getLogger().log(LogSeverity.ERROR, transferRequest, null);
            throw new InternalError("DuplicateKeyException = operationalId is already in use");
        }
        Logger.getLogger().log(LogSeverity.INFO, transferRequest, operationId);
        return new OperationDto(operationId);
    }

    public OperationDto doConfirm(ConfirmRequestDto confirmRequest) {
        String operationId = confirmRequest.operationId().replace("\"", "");
        String code = confirmRequest.code().replace("\"", "");
        if (!cardRepository.confirmOperationId(operationId)) {
            Logger.getLogger().log(LogSeverity.ERROR, confirmRequest, null);
            throw new IllegalArgumentException("NotFoundException = operationalId was not found in transactional DB for PIN code: " + code);
        }
        Logger.getLogger().log(LogSeverity.INFO, confirmRequest, operationId);
        return new OperationDto(operationId);
    }
}
