package com.lwerl.atm.command;

import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.monitor.Monitor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GetBalanceCommand implements Command {
    public static final String NAME = "getBalance";

    private List<Cell> cells;
    private Monitor monitor;
    private AtomicInteger container;

    public GetBalanceCommand(List<Cell> cells, Monitor monitor) {
        this.cells = cells;
        this.monitor = monitor;
    }

    public GetBalanceCommand(List<Cell> cells, AtomicInteger container) {
        this.cells = cells;
        this.container = container;
    }

    @Override
    public void execute() {
        int totalAmount = cells.stream().mapToInt(cell -> cell.getNumbersOfBanknotes() * cell.getBanknoteDenomination()).sum();
        if (container == null) {
            monitor.printMessage("В банкомате: " + totalAmount);
        } else {
            container.set(container.get() + totalAmount);
        }
    }
}
