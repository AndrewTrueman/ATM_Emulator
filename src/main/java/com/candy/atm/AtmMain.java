package com.candy.atm;

import com.candy.atm.actions.Authorization;
import com.candy.atm.actions.Balance;
import com.candy.atm.actions.Deposit;
import com.candy.atm.data.DataRepositoryImpl;

import java.util.Scanner;

public class AtmMain {
    public static void main(String[] args) {
        DataRepositoryImpl dataRepository = new DataRepositoryImpl();
        Authorization authorization = new Authorization(dataRepository);
        Balance balance = new Balance(dataRepository);
        Deposit deposit = new Deposit(dataRepository);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите номер карты:");
        String cardNumber = scanner.nextLine();

        int attempts = 0;
        boolean authorized = false;
        while (attempts < dataRepository.getMaxAttempts() && !authorized) {
            System.out.println("Введите ПИН-код:");
            int pinCode = Integer.parseInt(scanner.nextLine());

            if (authorization.authorize(cardNumber, pinCode)) {
                authorized = true;
                System.out.println("Авторизация успешна.");

                // Пример проверки баланса
                System.out.println("Ваш баланс: " + balance.checkBalance(cardNumber));

                // Пример пополнения баланса
                System.out.println("Введите сумму для пополнения:");
                double amount = Double.parseDouble(scanner.nextLine());

                if (deposit.deposit(cardNumber, amount)) {
                    System.out.println("Новый баланс: " + balance.checkBalance(cardNumber));
                }
            } else {
                attempts++;
                if (attempts >= dataRepository.getMaxAttempts()) {
                    System.out.println("Превышено количество попыток ввода ПИН-кода. Карта заблокирована.");
                }
            }
        }

        scanner.close();
    }
}
