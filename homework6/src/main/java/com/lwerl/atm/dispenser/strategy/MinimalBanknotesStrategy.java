package com.lwerl.atm.dispenser.strategy;

import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;

import java.util.List;

public class MinimalBanknotesStrategy implements DispenserStrategy {
    @Override
    public int[] getBanknoteCounts(int amount, List<Cell> cells) throws NotEnoughBanknotesException, NotValidAmountInputException {
        int tempAmount = amount;
        int[] banknoteCount = new int[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            int wishCount = tempAmount / cell.getBanknoteDenomination();
            int realCount = wishCount < cell.getNumbersOfBanknotes() ? wishCount : cell.getNumbersOfBanknotes();
            banknoteCount[i] = realCount;
            tempAmount -= realCount * cell.getBanknoteDenomination();
        }
        if (tempAmount == 0) {
            return banknoteCount;
        } else {
            boolean isAmountValid = false;
            for(Cell cell : cells) {
                if (tempAmount % cell.getBanknoteDenomination() == 0) {
                    isAmountValid = true;
                }
            }
            if (isAmountValid) {
                throw new NotEnoughBanknotesException();
            } else {
                throw new NotValidAmountInputException();
            }
        }
    }
}
