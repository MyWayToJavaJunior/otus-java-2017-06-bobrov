package com.lwerl.atm.keyboard;

import com.lwerl.atm.exception.WrongAmountInputException;

public interface Keyboard {

    int inputNumber() throws WrongAmountInputException;

    String inputCommand();
}
