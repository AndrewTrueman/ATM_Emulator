package com.candy.atm.actions;

import com.candy.atm.dto.SessionData;

public interface Action {
    void execute(SessionData data);
    String getName();

}
