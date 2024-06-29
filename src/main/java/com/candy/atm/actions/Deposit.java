package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;

public class Deposit {
    private final DataRepository dataRepository;
    private final double maxDepositAmount;

    public Deposit(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.maxDepositAmount = dataRepository.getMaxDepositAmount();
    }

    public boolean deposit(String cardNumber, double amount) {
        if (amount <= 0 || amount > maxDepositAmount) {
            System.out.println("Сумма пополнения должна быть положительной и не превышать " + maxDepositAmount + ".");
            return false;
        }

        CardDto card = dataRepository.getCardByNumber(cardNumber);
        if (card != null) {
            card.setBalance(card.getBalance() + amount);
            dataRepository.updateCard(card);
            System.out.println("Баланс успешно пополнен.");
            return true;
        } else {
            System.out.println("Карта не найдена.");
            return false;
        }
    }
}
