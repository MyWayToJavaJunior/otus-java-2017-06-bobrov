package com.lwerl.atm.cell;

/**
 * Created by lWeRl on 15.07.2017.
 */
public interface Cell {
    int getBanknotesFromCell(int count);

    int getNumbersOfBanknotes();

    int getCellBanknoteDenomination();

    void increaseNumbersOfBanknotes(int count);
}
