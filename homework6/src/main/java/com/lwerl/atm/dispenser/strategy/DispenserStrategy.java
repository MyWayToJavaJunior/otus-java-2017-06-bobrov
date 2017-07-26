package com.lwerl.atm.dispenser.strategy;

import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;

import java.util.List;

public interface DispenserStrategy {
    int[] getBanknoteCounts(int amount, List<Cell> cells) throws NotEnoughBanknotesException, NotValidAmountInputException;
}
