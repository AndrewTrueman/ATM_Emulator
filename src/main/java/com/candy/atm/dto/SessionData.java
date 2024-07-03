package com.candy.atm.dto;

import lombok.*;

@Data
public class SessionData {

    String cardNumber;
    CardDto cardDto;
    boolean authorized;
}
