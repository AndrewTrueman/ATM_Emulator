package com.candy.atm;

import com.candy.atm.actions.*;
import com.candy.atm.data.DataRepositoryImpl;
import com.candy.atm.dto.SessionData;

import java.util.List;
import java.util.Scanner;

public class AtmMain {

    private final DataRepositoryImpl dataRepository;
    private final List<Action> actions;
    private final Action authorization;
    private final Scanner scanner;


    public AtmMain() {
        dataRepository = new DataRepositoryImpl();
        authorization = new Authorization(dataRepository);
        actions = List.of(new Balance(), new Deposit(dataRepository), new Withdraw(dataRepository, dataRepository.getAtmLimit()));
        scanner = new Scanner(System.in);
    }

    public void start() {

        do {
            try {
                run();
            } catch (Exception e) {
                System.out.println("Ошибка! Попробуйте еще раз!");
            }
        } while ("Y".equalsIgnoreCase(getExit()));
    }

    private void run() {
        System.out.println("Добро пожаловать");
        SessionData session = new SessionData();
        session.setCardNumber(getCardNumber());
        authorization.execute(session);
        do {
            printMenu();
            actions.get(getChoice()).execute(session);
        } while ("Y".equalsIgnoreCase(getExit()));
    }

    private void printMenu() {
        int numOfOperation = 0;
        for (Action action : actions) {
            System.out.println(numOfOperation++ + ". " + action.getName());
        }
    }

    private int getChoice() {
        System.out.print("Введедите номер операции которую хотите выполнить: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            throw new IllegalArgumentException("Только цифры!");
        }
    }

    private String getCardNumber() {
        System.out.print("Введите номер карты: ");
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при вводе карты");
        }
    }

    private String getExit() {
        System.out.println("Продолжить? (Y/N)");
        return scanner.nextLine();
    }
}

