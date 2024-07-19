package ru.netology.moneytransferservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransferservice.config.ConfirmRequestResolver;
import ru.netology.moneytransferservice.config.TransferRequestResolver;
import ru.netology.moneytransferservice.repository.ConfirmRequestDto;
import ru.netology.moneytransferservice.repository.OperationDto;
import ru.netology.moneytransferservice.repository.TransferRequestDto;
import ru.netology.moneytransferservice.service.MoneyTransferService;

@CrossOrigin
// (allowedHeaders = {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@Validated
@AllArgsConstructor
public class MoneyTransferController {
    private final MoneyTransferService service;

    @PostMapping("/transfer")
    public OperationDto doTransfer(@TransferRequestResolver @Valid TransferRequestDto transferRequest) {
        return service.doTransfer(transferRequest);
    }

    @PostMapping("/confirmOperation")
    public OperationDto doTransfer(@ConfirmRequestResolver @Valid ConfirmRequestDto confirmRequest) {
        return service.doConfirm(confirmRequest);
    }
}