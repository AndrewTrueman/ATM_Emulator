package com.candy.atm;

public interface AtmUi {
    void displayWelcomeMessage();
    void displayCardNumberPrompt();
    void displayPinPrompt();
    void displayAuthorizationSuccess();
    void displayAuthorizationFailure(int attemptsLeft);
    void displayCardNotFound();
    void displayCardBlocked();
    void displayBalance(double balance);
    void displayDepositPrompt();
    void displayDepositSuccess(double amount, double newBalance);
    void displayDepositLimitExceeded();
    void displayWithdrawPrompt();
    void displayWithdrawSuccess(double amount, double newBalance);
    void displayInsufficientFunds();
    void displayInvalidChoice();
    void displayInvalidPin();
    void displayInvalidAmount();
    void displayExitMessage();
    void displayMenu();
}
