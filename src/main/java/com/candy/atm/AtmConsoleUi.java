package com.candy.atm;

public class AtmConsoleUi implements AtmUi {
    @Override
    public void displayWelcomeMessage() {
        System.out.println("Добро пожаловать в банкомат!");
    }

    @Override
    public void displayCardNumberPrompt() {
        System.out.print("Введите номер карты (или 'exit' для выхода): ");
    }

    @Override
    public void displayPinPrompt() {
        System.out.print("Введите ПИН-код: ");
    }

    @Override
    public void displayAuthorizationSuccess() {
        System.out.println("Авторизация прошла успешно.");
    }

    @Override
    public void displayAuthorizationFailure(int attemptsLeft) {
        System.out.println("Неверный ПИН-код. Осталось попыток: " + attemptsLeft);
    }

    @Override
    public void displayCardNotFound() {
        System.out.println("Карта не найдена.");
    }

    @Override
    public void displayCardBlocked() {
        System.out.println("Карта заблокирована на 24 часа.");
    }

    @Override
    public void displayBalance(double balance) {
        System.out.println("Ваш баланс: " + balance);
    }

    @Override
    public void displayDepositPrompt() {
        System.out.print("Введите сумму для пополнения: ");
    }

    @Override
    public void displayDepositSuccess(double amount, double newBalance) {
        System.out.println("Сумма " + amount + " успешно зачислена. Новый баланс: " + newBalance);
    }

    @Override
    public void displayDepositLimitExceeded() {
        System.out.println("Превышен лимит пополнения.");
    }

    @Override
    public void displayWithdrawPrompt() {
        System.out.print("Введите сумму для снятия: ");
    }

    @Override
    public void displayWithdrawSuccess(double amount, double newBalance) {
        System.out.println("Сумма " + amount + " успешно снята. Новый баланс: " + newBalance);
    }

    @Override
    public void displayInsufficientFunds() {
        System.out.println("Недостаточно средств.");
    }

    @Override
    public void displayInvalidChoice() {
        System.out.println("Неверный выбор. Попробуйте снова.");
    }

    @Override
    public void displayInvalidPin() {
        System.out.println("Неверный ПИН-код. Введите числовое значение.");
    }

    @Override
    public void displayInvalidAmount() {
        System.out.println("Неверная сумма. Введите числовое значение.");
    }

    @Override
    public void displayExitMessage() {
        System.out.println("Выход из системы. Спасибо за использование банкомата.");
    }

    @Override
    public void displayMenu() {
        System.out.println("Выберите операцию:");
        System.out.println("0 - Выйти");
        System.out.println("1 - Проверить баланс");
        System.out.println("2 - Пополнить счет");
        System.out.println("3 - Снять средства");
    }
}
