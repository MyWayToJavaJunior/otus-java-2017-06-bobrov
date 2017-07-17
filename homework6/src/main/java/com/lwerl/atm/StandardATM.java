package com.lwerl.atm;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.banknote.StandardBanknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.cell.StandardCell;

import java.util.ArrayList;
import java.util.List;

public class StandardATM extends ATM {

    public StandardATM(int amountInEachCell) {
        cells = new Cell[StandardBanknote.values().length];
        for (int i = 0; i < StandardBanknote.values().length; i++) {
            cells[i] = new StandardCell(StandardBanknote.values()[i], amountInEachCell);
        }
    }


    @Override
    public void start() {
        System.out.println("MOCK");
    }

    @Override
    protected List<Banknote> giveCash(int amount) {
        return new ArrayList<>();
    }
}
