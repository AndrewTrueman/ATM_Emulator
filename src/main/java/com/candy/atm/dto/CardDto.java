package com.candy.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDto {

    private String cardNumber;
    private int pinCode;
    private double balance;
}

