package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

public interface DataRepository {

     CardDto getCardByNumber(String cardNumber);

     void updateCard(CardDto card);

     boolean isCardBlocked(String cardNumber);

     void blockCard(String cardNumber);

     void unblockCard(String cardNumber);

     int getMaxAttempts();

     double getMaxDepositAmount();
     double getAtmLimit();

}
