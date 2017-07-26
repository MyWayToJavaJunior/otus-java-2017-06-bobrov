package com.lwerl.atm.cell;

import com.lwerl.atm.banknote.Banknote;

import java.util.List;

public interface Cell {
    List<Banknote> getBanknotesFromCell(int count);

    int getNumbersOfBanknotes();

    void setNumbersOfBanknotes(int count);

    int getBanknoteDenomination();

    void increaseNumbersOfBanknotes(int count);

}
