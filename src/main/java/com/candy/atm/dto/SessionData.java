package com.candy.atm.dto;

import lombok.*;

@Data
public class SessionData {

    private String cardNumber;
    private CardDto cardDto;
    private boolean authorized;
}
