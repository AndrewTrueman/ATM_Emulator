package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;

public class Withdraw {
    private final DataRepository dataRepository;
    private final double atmLimit;

    public Withdraw(DataRepository dataRepository, double atmLimit) {
        this.dataRepository = dataRepository;
        this.atmLimit = atmLimit;
    }

    public boolean withdraw(String cardNumber, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма снятия должна быть положительной.");
            return false;
        }

        CardDto card = dataRepository.getCardByNumber(cardNumber);
        if (card != null) {
            if (card.getBalance() < amount) {
                System.out.println("Недостаточно средств на счете.");
                return false;
            } else if (amount > atmLimit) {
                System.out.println("Превышен лимит снятия средств в банкомате.");
                return false;
            } else {
                card.setBalance(card.getBalance() - amount);
                dataRepository.updateCard(card);
                System.out.println("Средства успешно сняты. Текущий баланс: " + card.getBalance());
                return true;
            }
        } else {
            System.out.println("Карта не найдена.");
            return false;
        }
    }
}
