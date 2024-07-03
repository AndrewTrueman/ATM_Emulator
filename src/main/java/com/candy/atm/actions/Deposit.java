package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.CardDto;
import com.candy.atm.dto.SessionData;

import java.util.Scanner;

public class Deposit implements Action {
    private final DataRepository dataRepository;
    private final double maxDepositAmount;
    private final Scanner scanner;

    public Deposit(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.maxDepositAmount = dataRepository.getMaxDepositAmount();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(SessionData data) {
        double newBalance = data.getCardDto().getBalance() + getDepositAmount();
        data.getCardDto().setBalance(newBalance);
        dataRepository.updateCard(data.getCardDto());
        System.out.println("Баланс успешно пополнен.");
    }

    @Override
    public String getName() {
        return "Положить деньги на счет.";
    }

    private double getDepositAmount() {
        try {
            System.out.print("Введите сумму депозита");
            double depositAmount = Double.parseDouble(scanner.nextLine());

            if (depositAmount <= 0 || depositAmount > maxDepositAmount) {
                throw new IllegalArgumentException("Сумма пополнения должна быть положительной и не превышать " + maxDepositAmount + ".");
            }
            return depositAmount;
        } catch (IllegalArgumentException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать сумму.");
        }
    }
}
