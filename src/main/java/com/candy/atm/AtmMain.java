package com.candy.atm;

import com.candy.atm.actions.Authorization;
import com.candy.atm.actions.Balance;
import com.candy.atm.actions.Deposit;
import com.candy.atm.actions.Withdraw;
import com.candy.atm.data.DataRepositoryImpl;
import com.candy.atm.dto.CardDto;


import java.util.Scanner;

public class AtmMain {
    public static void main(String[] args) {
        DataRepositoryImpl dataRepository = new DataRepositoryImpl();
        Authorization authorization = new Authorization(dataRepository);
        Balance balance = new Balance(dataRepository);
        Deposit deposit = new Deposit(dataRepository);
        Withdraw withdraw = new Withdraw(dataRepository, 10000); // Лимит средств на снятие
        AtmUi atmUI = new AtmConsoleUi();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            atmUI.displayWelcomeMessage();
            atmUI.displayCardNumberPrompt();
            String cardNumber = scanner.nextLine();

            if (cardNumber.equalsIgnoreCase("exit")) {
                atmUI.displayExitMessage();
                break;
            }

            CardDto card = dataRepository.getCardByNumber(cardNumber);
            if (card == null) {
                atmUI.displayCardNotFound();
                continue;
            }

            int attempts = 0;
            boolean authorized = false;
            while (attempts < dataRepository.getMaxAttempts() && !authorized) {
                atmUI.displayPinPrompt();
                try {
                    int pinCode = Integer.parseInt(scanner.nextLine());

                    if (authorization.authorize(cardNumber, pinCode)) {
                        authorized = true;
                        atmUI.displayAuthorizationSuccess();

                        do {
                            atmUI.displayMenu();
                            int choice;
                            try {
                                choice = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                atmUI.displayInvalidChoice();
                                continue;
                            }

                            switch (choice) {
                                case 0:
                                    atmUI.displayExitMessage();
                                    exit = true;
                                    break;
                                case 1:
                                    atmUI.displayBalance(balance.checkBalance(cardNumber));
                                    break;
                                case 2:
                                    atmUI.displayDepositPrompt();
                                    double depositAmount;
                                    try {
                                        depositAmount = Double.parseDouble(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        atmUI.displayInvalidAmount();
                                        continue;
                                    }
                                    if (deposit.deposit(cardNumber, depositAmount)) {
                                        atmUI.displayDepositSuccess(depositAmount, balance.checkBalance(cardNumber));
                                    } else {
                                        atmUI.displayDepositLimitExceeded();
                                    }
                                    break;
                                case 3:
                                    atmUI.displayWithdrawPrompt();
                                    double withdrawAmount;
                                    try {
                                        withdrawAmount = Double.parseDouble(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        atmUI.displayInvalidAmount();
                                        continue;
                                    }
                                    if (withdraw.withdraw(cardNumber, withdrawAmount)) {
                                        atmUI.displayWithdrawSuccess(withdrawAmount, balance.checkBalance(cardNumber));
                                    } else {
                                        atmUI.displayInsufficientFunds();
                                    }
                                    break;
                                default:
                                    atmUI.displayInvalidChoice();
                                    break;
                            }
                        } while (!exit && authorized);

                    } else {
                        attempts++;
                        if (attempts >= dataRepository.getMaxAttempts()) {
                            atmUI.displayCardBlocked();
                            dataRepository.blockCard(cardNumber);
                        } else {
                            atmUI.displayAuthorizationFailure(dataRepository.getMaxAttempts() - attempts);
                        }
                    }
                } catch (NumberFormatException e) {
                    atmUI.displayInvalidPin();
                }
            }
        }

        scanner.close();
    }
}
