package com.lwerl.atm.command;

import com.lwerl.atm.cell.Cell;

import java.util.List;

public class InitATMCommand implements Command {

    public static final String NAME = "reInit";

    private int initAmount;
    private List<Cell> cells;

    public InitATMCommand(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public void execute() {
        for (Cell cell : cells) {
            cell.setNumbersOfBanknotes(initAmount);
        }
    }
}
