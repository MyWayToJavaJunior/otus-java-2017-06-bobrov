package com.lwerl.atm;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.dispenser.Dispenser;
import com.lwerl.atm.monitor.Monitor;
import com.lwerl.atm.receiver.Receiver;

import java.util.List;


public abstract class ATM {

    protected Monitor monitor;
    protected Dispenser dispenser;
    protected Receiver receiver;
    protected Cell[] cells;


    protected int getBalance() {
        int balance = 0;
        for (Cell cell : cells) {
            balance += cell.getNumbersOfBanknotes() * cell.getCellBanknoteDenomination();
        }
        return balance;
    }

    protected void putCash() {
        List<Banknote> cash = receiver.takeMoney();
        for (Banknote banknote : cash) {
            for (Cell cell : cells) {
                if (cell.getCellBanknoteDenomination() == banknote.getDenomination()) {
                    cell.increaseNumbersOfBanknotes(1);
                    break;
                }
            }
        }
    }

    protected boolean isSystemOk() {
        if (monitor == null || dispenser == null || receiver == null || cells == null) {
            return false;
        }
        for (Cell cell : cells) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    public abstract void start();
    protected abstract List<Banknote> giveCash(int amount);
}
