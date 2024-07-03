package com.candy.atm;

import com.candy.atm.actions.*;
import com.candy.atm.data.DataRepositoryImpl;
import com.candy.atm.dto.CardDto;
import com.candy.atm.dto.SessionData;


import java.util.List;
import java.util.Scanner;

public class AtmMain {
    private final DataRepositoryImpl dataRepository;

    private final AtmUi atmUI;

    private final List<Action> actions;

    private final Action authorization;
    private final Scanner scanner;

    public AtmMain() {
        dataRepository = new DataRepositoryImpl();
        authorization = new Authorization(dataRepository);
        atmUI = new AtmConsoleUi();
        actions = List.of(new Balance(), new Deposit(dataRepository), new Withdraw(dataRepository, 10000));
        scanner = new Scanner(System.in);
    }

    public void start() {
        do {
           //getExit();
            try {
                run();
                System.out.println("Продолжить? (Y/N)");
                scanner.nextLine();
                if ("Y".equalsIgnoreCase(scanner.nextLine())) {
                    //getExit();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } while (true);
    }

    private void run() {
        atmUI.displayWelcomeMessage();
        SessionData data = new SessionData();
        dataRepository.getCardByNumber(getCard());
        CardDto card;
        do {
            String cardNumber = getCard();
            card = dataRepository.getCardByNumber(cardNumber);
            if (card == null || card != dataRepository.getCardByNumber(cardNumber)) {
                atmUI.displayCardNotFound();
            }
        } while (card == null);
        data.setCardDto(card); //setCardDto(getCard)
        authorization.execute(data);
        do {
            printMenu(data);
        } while (true);
    }
    private void printMenu(SessionData data){
        final int[] numOfOperation = {0};
        actions.forEach(action -> {
            System.out.println(numOfOperation[0] + ". " + action.getName());
            numOfOperation[0]++;
        });
        actions.get(getChoice()).execute(data);

    }
    private int getChoice() {
        System.out.print("Введедите номер операции которую хотите выполнить");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            throw new IllegalArgumentException("Только цифры!");

        }
    }
    private String getCard() {
        System.out.print("ВВедите номер карты: ");
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при вводе карты");
        }
    }
    private void getExit(){
        String des = scanner.nextLine();
        if (des.equalsIgnoreCase("y"));
        System.exit(0);
    }
}

