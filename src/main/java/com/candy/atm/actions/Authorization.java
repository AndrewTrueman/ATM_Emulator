package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
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
    public void execute(SessionData data) {
        if (dataRepository.isCardBlocked(data.getCardNumber())) {
            throw new IllegalArgumentException("Карта заблокирована.");

        }
        int attempt = 0;
        do {
            int pin = getPin();
            if (pin == data.getCardDto().getPinCode()) {
                data.setAuthorized(true);
                return;
            }
            if (pin != data.getCardDto().getPinCode()){
                System.out.println("Вы ввели неверный пин-код, попробуйте еще раз!");
            }
            attempt++;
        } while (attempt < maxAttempts && !data.isAuthorized());

        if (!data.isAuthorized()) {
            dataRepository.blockCard(data.getCardDto().getCardNumber());
            throw new IllegalArgumentException("Карта заблокирована из-за превышения количества попыток ввода ПИН-кода.");
        }
    }

    @Override
    public String getName() {
        return "Авторизация";
    }

    private int getPin() {
        System.out.print("ВВедите Пин-код: ");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            throw new IllegalArgumentException("Только цифры!");

        }
    }
}
