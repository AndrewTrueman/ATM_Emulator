package com.candy.atm.actions;

import com.candy.atm.data.DataRepository;
import com.candy.atm.dto.SessionData;

import java.util.Scanner;

public class Withdraw implements Action {
    private final DataRepository dataRepository;
    private final double atmLimit;
    private final Scanner scanner;


    public Withdraw(DataRepository dataRepository, double atmLimit) {
        this.dataRepository = dataRepository;
        this.atmLimit = atmLimit;
        this.scanner = new Scanner(System.in);

    }

    @Override
    public void execute(SessionData data) {
        double withdrawAmount = getWithdrawAmount();
        validate(data, withdrawAmount);
        double newBalance = data.getCardDto().getBalance() - withdrawAmount;
        data.getCardDto().setBalance(newBalance);
        dataRepository.updateCard(data.getCardDto());
        System.out.println("Cредства успешно сняты.");

    }

    @Override
    public String getName() {
        return "Снять средства с карты.";
    }

    private double getWithdrawAmount() {
        try {
            System.out.print("Введите сумму для снятия средств: ");
            return Double.parseDouble(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать сумму");
        }

    }

    private void validate(SessionData data, double withdrawAmount) {

        if (withdrawAmount <= 0) {
            throw new IllegalArgumentException("Сумма не должна быть меньше 0.");
        }
        if (withdrawAmount > atmLimit) {
            throw new IllegalArgumentException(String.format("Сумма не должна быть больше %s .", atmLimit));
        }
        if (withdrawAmount > data.getCardDto().getBalance()) {
            throw new IllegalArgumentException("Не достаточно средств для снятия.");
        }


    }

}

