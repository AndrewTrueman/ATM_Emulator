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
        actions = List.of(new Balance(), new Deposit(dataRepository), new Withdraw(dataRepository));
        scanner = new Scanner(System.in);
    }

    public void start() {

        do {
            executeWithErrorHandling(); //Запуск метода Run с проверкой. Проверку вынесли в отдельный метод для читабильности
        } while ("Y".equalsIgnoreCase(getExit()));
    }

    private void run() {
        System.out.println("Добро пожаловать");
        SessionData session = new SessionData();
        session.setCardNumber(getCardNumber());
        authorization.execute(session); // передаем текущую сессию в метод
        do {
            printMenu();
            actions.get(getChoice()).execute(session); // передаем номер операции выбранной пользователем и исполняем ее
        } while ("Y".equalsIgnoreCase(getExit()));
    }

    private void printMenu() { // Вывод меню операций
        int numOfOperation = 0;
        for (Action action : actions) {
            System.out.println(numOfOperation++ + ". " + action.getName()); // Выводим меню действий пользователя.
            //Все действия хранятся в коллекции, выводим их по очереди и добавляем номер операции
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

    private void executeWithErrorHandling() {
        try {
            run();
        } catch (Exception e) {
            System.out.println("Ошибка! Попробуйте еще раз!");
        }
    }
}

