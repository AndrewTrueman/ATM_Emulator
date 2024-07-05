package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;
import com.candy.atm.dto.SessionData;

import java.util.Scanner;

public class Authorization implements Action {
    private final DataRepository dataRepository;
    private final int maxAttempts;
    private final Scanner scanner;


    public Authorization(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.maxAttempts = dataRepository.getMaxAttempts();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(SessionData session) {

        if (dataRepository.isCardBlocked(session.getCardNumber())) {
            throw new IllegalArgumentException("Карта заблокирована.");
        }
        CardDto card = dataRepository.getCardByNumber(session.getCardNumber());
        int attempt = 0;
        do {
            if (getPin() == card.getPinCode()) {
                session.setAuthorized(true);
                session.setCardDto(card);
                return;
            }
            System.out.println("Вы ввели неверный пин-код, попробуйте еще раз!");
            attempt++;

        } while (attempt < maxAttempts && !session.isAuthorized());

        if (!session.isAuthorized()) {
            dataRepository.blockCard(card.getCardNumber());
            throw new IllegalArgumentException("Карта заблокирована из-за превышения количества попыток ввода ПИН-кода.");
        }
    }

    @Override
    public String getName() {
        return "Авторизация";
    }

    private int getPin() {
        System.out.print("Введите Пин-код: ");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            throw new IllegalArgumentException("Только цифры!");

        }
    }
}
