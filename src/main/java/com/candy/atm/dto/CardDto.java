package com.candy.atm.dto;

public class CardDto {
    private String cardNumber;
    private int pin;
    private double balance;
    private boolean isBlocked;


    public CardDto(String cardNumber, int pin,double balance){
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }
    public String getCardNumber() {
        return cardNumber;
    }

    public int getPinCode() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
