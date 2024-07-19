package ru.netology.moneytransferservice.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.moneytransferservice.logger.LogSeverity;
import ru.netology.moneytransferservice.logger.Logger;
import ru.netology.moneytransferservice.repository.*;

@RequiredArgsConstructor
public class MoneyTransferService {
    @Getter
    private int operationId = 1000; //int operationId = Math.abs(new Random().nextInt());
    private final CardRepository cardRepository;

    public OperationDto doTransfer(TransferRequestDto transferRequest) {
        Card cardFrom = cardRepository.getCard(transferRequest.cardFrom());
        Card cardTo = cardRepository.getCardByNumber(transferRequest.cardToNumber().replace("\"", ""));

        if (cardFrom == null) {
            Logger.getLogger().log(LogSeverity.WARN, transferRequest, -1);
            throw new IllegalArgumentException("NotFoundException = cardFrom not found or wrong expiration / CVV data");
        }

        if (cardTo == null) {
            Logger.getLogger().log(LogSeverity.WARN, transferRequest, -1);
            throw new IllegalArgumentException("NotFoundException = cardTo not found or wrong expiration / CVV data");
        }

        increaseOperationId();
        if (!cardRepository.saveOperationId(operationId)) {
            Logger.getLogger().log(LogSeverity.ERROR, transferRequest, -1);
            throw new InternalError("DuplicateKeyException = operationId is already in use");
        }

        Logger.getLogger().log(LogSeverity.INFO, transferRequest, operationId);
        return new OperationDto(operationId);
    }

    private synchronized void increaseOperationId() {
        ++operationId;
    }

    public OperationDto doConfirm(ConfirmRequestDto confirmRequest) {
        int operationId = confirmRequest.operationId();
        String code = confirmRequest.code().replace("\"", "");

        if (!cardRepository.confirmOperationId(operationId)) {
            Logger.getLogger().log(LogSeverity.ERROR, confirmRequest, -1);
            throw new IllegalArgumentException("NotFoundException = operationId was not found in transactional DB for PIN code: " + code);
        }

        Logger.getLogger().log(LogSeverity.INFO, confirmRequest, operationId);
        return new OperationDto(operationId);
    }
}
