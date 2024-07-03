package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;
import com.candy.atm.dto.SessionData;

public class Balance implements Action {

    @Override
    public void execute(SessionData data) {
        System.out.println("Ваш баланс: " + data.getCardDto().getBalance());
    }

    @Override
    public String getName() {

        return "Вывести текущий баланс.";
    }
}
