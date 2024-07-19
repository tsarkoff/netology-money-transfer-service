package ru.netology.moneytransferservice.repository;

public interface CardRepository {
    Card getCard(Card card);
    Card getCardByNumber(String number);
    boolean saveOperationId(int operationId);
    boolean confirmOperationId(int operationId);
}
