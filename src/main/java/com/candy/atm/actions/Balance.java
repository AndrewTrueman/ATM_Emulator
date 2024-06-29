package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;

public class Balance {
    private final DataRepository dataRepository;

    public Balance(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public double checkBalance(String cardNumber) {
        CardDto card = dataRepository.getCardByNumber(cardNumber);
        if (card != null) {
            return card.getBalance();
        } else {
            System.out.println("Карта не найдена.");
            return -1;
        }
    }
}
