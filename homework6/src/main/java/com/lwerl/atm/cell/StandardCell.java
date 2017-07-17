package com.lwerl.atm.cell;

import com.lwerl.atm.banknote.Banknote;

public class StandardCell implements Cell {

    private Banknote banknote;
    private int count;

    public StandardCell(Banknote banknote, int count) {
        this.banknote = banknote;
        this.count = count;
    }

    @Override
    public int getBanknotesFromCell(int count) {
        int result;
        if (count < this.count) {
            result = count;
            this.count -= count;
        } else {
            result = this.count;
            this.count = 0;
        }
        return result;
    }

    @Override
    public int getNumbersOfBanknotes() {
        return count;
    }

    @Override
    public int getCellBanknoteDenomination() {
        return banknote.getDenomination();
    }

    @Override
    public void increaseNumbersOfBanknotes(int count) {
        this.count += count;
    }
}
