package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

public interface DataRepository {
     void blockCard(String cardNumber);
     CardDto getUserByCardNumber(String cardNumber);
     void updateUser(CardDto user);
     boolean isCardBlocked(String cardNumber);
     void unblockCard(String cardNumber);
}
