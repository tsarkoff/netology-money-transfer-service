package ru.netology.moneytransferservice.repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CardRepositoryImpl implements CardRepository {
    private static final Set<Integer> opIds = ConcurrentHashMap.newKeySet();
    private static final List<Card> cards = List.of(
            new Card("2222000022220000", "02/25", "222"),
            new Card("3333000033330000", "03/25", "333"),
            new Card("4444000044440000", "04/25", "444"),
            new Card("5555000055550000", "05/25", "555")
    );

    public Card getCard(Card card) {
        for (Card c : cards)
            if (c.equals(card))
                return c;
        return null;
    }

    public Card getCardByNumber(String number) {
        for (Card c : cards) {
            String num = c.getNumber();
            if (num.equals(number))
                return c;
        }
        return null;
    }

    public synchronized boolean saveOperationId(int operationId) {
        return opIds.add(operationId);
    }

    public synchronized boolean confirmOperationId(int operationId) {
        return opIds.remove(operationId);
    }
}
