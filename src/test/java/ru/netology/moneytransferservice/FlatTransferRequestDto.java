package ru.netology.moneytransferservice;

import lombok.AllArgsConstructor;
import ru.netology.moneytransferservice.repository.Amount;

@AllArgsConstructor
public class FlatTransferRequestDto {
    private String cardFormNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;
}
