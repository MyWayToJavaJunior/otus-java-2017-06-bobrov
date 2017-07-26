package com.lwerl.atm.keyboard;

import com.lwerl.atm.exception.WrongAmountInputException;

import java.util.Scanner;

public class StandardKeyboard implements Keyboard {

    private Scanner sc = new Scanner(System.in);

    @Override
    public int inputNumber() throws WrongAmountInputException {
        int result;
        try {
            result = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            throw new WrongAmountInputException(e);
        }
        return result;
    }

    @Override
    public String inputCommand() {

        String result;
        result = sc.nextLine();
        return result;
    }
}
