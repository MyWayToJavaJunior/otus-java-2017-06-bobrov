package com.lwerl.atm.command;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.monitor.Monitor;
import com.lwerl.atm.receiver.Receiver;

import java.util.Iterator;
import java.util.List;


public class PutCashCommand implements Command {

    public static final String NAME = "putCash";

    private Receiver receiver;
    private Monitor monitor;
    private List<Cell> cells;

    public PutCashCommand(Receiver receiver, Monitor monitor, List<Cell> cells) {
        this.receiver = receiver;
        this.monitor = monitor;
        this.cells = cells;
    }

    @Override
    public void execute() {
        monitor.printMessage("Введите банкноты:");
        List<Banknote> banknotes = receiver.takeMoney();
        int amount = 0;
        for (Banknote banknote : banknotes) {
            amount += banknote.getDenomination();
        }
        if (amount == 0) {
            monitor.printMessage("Введенные купюры не соответвуют стандарту!");
        } else {
            monitor.printMessage("Введенна сумма: " + amount);
            for (Cell cell : cells) {
                for (Iterator<Banknote> iterator = banknotes.iterator(); iterator.hasNext(); ) {
                    Banknote current = iterator.next();
                    if (current.getDenomination() == cell.getBanknoteDenomination()) {
                        cell.increaseNumbersOfBanknotes(1);
                        iterator.remove();
                    }
                }
            }
            monitor.printMessage("Наличные положены.");
        }
    }
}
