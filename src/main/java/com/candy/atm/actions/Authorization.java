package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;

import java.util.HashMap;
import java.util.Map;

public class Authorization {
    private final DataRepository dataRepository;
    private final int maxAttempts;
    private Map<String, Integer> attemptCounts = new HashMap<>();

    public Authorization(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.maxAttempts = dataRepository.getMaxAttempts();
    }

    public boolean authorize(String cardNumber, int pinCode) {
        CardDto card = dataRepository.getCardByNumber(cardNumber);
        if (card == null) {
            System.out.println("Карта не найдена.");
            return false;
        }

        if (dataRepository.isCardBlocked(cardNumber)) {
            System.out.println("Карта заблокирована.");
            return false;
        }

        if (card.getPinCode() == pinCode) {
            attemptCounts.remove(cardNumber);
            return true;
        } else {
            attemptCounts.put(cardNumber, attemptCounts.getOrDefault(cardNumber, 0) + 1);
            if (attemptCounts.get(cardNumber) >= maxAttempts) {
                dataRepository.blockCard(cardNumber);
                System.out.println("Карта заблокирована из-за превышения количества попыток ввода ПИН-кода.");
            } else {
                System.out.println("Неверный номер карты или ПИН-код.");
            }
            return false;
        }
    }
}
