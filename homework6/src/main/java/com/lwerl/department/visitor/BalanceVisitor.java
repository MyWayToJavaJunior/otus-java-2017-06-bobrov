package com.lwerl.department.visitor;

import com.lwerl.atm.command.Command;
import com.lwerl.atm.command.GetBalanceCommand;
import com.lwerl.atm.exception.NotValidSecretKeyException;
import com.lwerl.department.ATMInfo;

import java.util.concurrent.atomic.AtomicInteger;

public class BalanceVisitor implements ATMVisitor {

    private AtomicInteger container = new AtomicInteger(0);

    public AtomicInteger getContainer() {
        return container;
    }

    @Override
    public void visit(final ATMInfo atmInfo) {
        try {
            Command command = new GetBalanceCommand(atmInfo.getAtm().getCells(atmInfo.getSecretKey()), container);
            atmInfo.getAtm().executeWithSecret(atmInfo.getSecretKey(), command);
        } catch (NotValidSecretKeyException e) {
            System.out.println("Приватная связь с банкоматом " + atmInfo.getAtm().toString() + " не установлена.");
        }
    }
}
