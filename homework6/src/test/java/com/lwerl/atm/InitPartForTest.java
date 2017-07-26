package com.lwerl.atm;


import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.banknote.StandardBanknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.cell.StandardCell;
import com.lwerl.atm.dispenser.strategy.MinimalBanknotesStrategy;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public abstract class InitPartForTest {

    protected Banknote banknote;
    protected Cell cell;
    protected List<Cell> cells;

    @Before
    public abstract void before();

    protected void initCell(int count) {
        banknote = StandardBanknote.P500;
        cell = new StandardCell(banknote, count);
    }

    protected void initCells(int initCount) {
        cells = new ArrayList<>();
        for (int i = 0; i < StandardBanknote.values().length; i++) {
            cells.add(new StandardCell(StandardBanknote.values()[i], initCount));
        }
    }

    @After
    public void after() {
        banknote = null;
        cell = null;
        cells = null;
    }
}
