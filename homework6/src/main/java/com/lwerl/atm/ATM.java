package com.lwerl.atm;

import com.lwerl.atm.banknote.StandardBanknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.command.*;
import com.lwerl.atm.dispenser.Dispenser;
import com.lwerl.atm.dispenser.StandardDispenser;
import com.lwerl.atm.exception.NotValidSecretKeyException;
import com.lwerl.atm.keyboard.Keyboard;
import com.lwerl.atm.keyboard.StandardKeyboard;
import com.lwerl.atm.monitor.Monitor;
import com.lwerl.atm.monitor.StandardMonitor;
import com.lwerl.atm.receiver.Receiver;
import com.lwerl.atm.receiver.StandardReceiver;
import com.lwerl.department.ATMInfo;
import com.lwerl.department.visitor.ATMVisitor;

import java.util.*;


public class ATM {

    protected Monitor monitor;
    protected Dispenser dispenser;
    protected Receiver receiver;
    protected List<Cell> cells;
    protected Keyboard keyboard;
    protected Map<String, Command> commandMap;
    protected String secretKey;
    protected Command onServiceCommand;

    public ATM(String secretKey) {
        this.secretKey = secretKey;
        this.monitor = new StandardMonitor();
        this.receiver = new StandardReceiver(new HashSet<>(Arrays.asList(StandardBanknote.values())));
        this.keyboard = new StandardKeyboard();
        this.onServiceCommand = new OnServiceCommand(monitor);
    }

    public List<Cell> getCells(String secretKey) throws NotValidSecretKeyException {
        if (secretKey.equals(this.secretKey)) {
            return cells;
        } else {
            throw new NotValidSecretKeyException();
        }
    }

    public void setCells(List<Cell> cells, String secretKey) throws NotValidSecretKeyException {
        if (secretKey.equals(this.secretKey)) {
            cells.sort((cell1, cell2) -> cell2.getBanknoteDenomination() - cell1.getBanknoteDenomination());
            this.cells = cells;
            this.dispenser = new StandardDispenser(this.cells);
        } else {
            throw new NotValidSecretKeyException();
        }
    }

    private void initCommand() {
        commandMap = new HashMap<>();
        commandMap.put(GetBalanceCommand.NAME, new GetBalanceCommand(cells, monitor));
        commandMap.put(GetCashCommand.NAME, new GetCashCommand(keyboard, monitor, dispenser));
        commandMap.put(PutCashCommand.NAME, new PutCashCommand(receiver, monitor, cells));
        commandMap.put(MenuCommand.NAME, new MenuCommand(monitor, commandMap, keyboard));
    }


    public void executeWithSecret(String key, Command command) throws NotValidSecretKeyException {
        if (key.equals(secretKey)) {
            command.execute();
        } else {
            throw new NotValidSecretKeyException();
        }
    }

    public void executeFromMenu() {
        if (commandMap == null) {
            initCommand();
        }
        if (cells == null) {
            onServiceCommand.execute();
        } else {
            commandMap.get(MenuCommand.NAME).execute();
        }
    }

    public void accept(ATMVisitor visitor, ATMInfo atmInfo) throws NotValidSecretKeyException {
        if (atmInfo.getSecretKey().equals(secretKey)) {
            visitor.visit(atmInfo);
        } else {
            throw new NotValidSecretKeyException();
        }
    }
}
