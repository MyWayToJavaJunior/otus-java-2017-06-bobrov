package com.lwerl.department.visitor;

import com.lwerl.atm.banknote.StandardBanknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.cell.StandardCell;
import com.lwerl.atm.exception.NotValidSecretKeyException;
import com.lwerl.department.ATMInfo;

import java.util.ArrayList;
import java.util.List;

public class InitVisitor implements ATMVisitor {

    @Override
    public void visit(final ATMInfo atmInfo) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < StandardBanknote.values().length; i++) {
            cells.add(new StandardCell(StandardBanknote.values()[i], atmInfo.getInitialCount()));
        }
        try {
            atmInfo.getAtm().setCells(cells, atmInfo.getSecretKey());
        } catch (NotValidSecretKeyException e) {
            System.out.println("Приватная связь с банкоматом " + atmInfo.getAtm().toString() + " не установлена.");
        }
    }
}
