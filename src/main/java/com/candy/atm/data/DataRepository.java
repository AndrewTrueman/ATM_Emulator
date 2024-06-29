package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

public interface DataRepository {
    private void getUserData(){

    }
    private void getBlockedUserData(){

    }
    private void getProperty(){

    }
     void blockCard(String cardNumber);
     CardDto getUserByCardNumber(String cardNumber);
     void updateUser(CardDto user);
     boolean isCardBlocked(String cardNumber);
     void unblockCard(String cardNumber);
}
