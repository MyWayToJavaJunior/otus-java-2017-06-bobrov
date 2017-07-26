package com.lwerl.department;

import com.lwerl.atm.exception.NotValidSecretKeyException;
import com.lwerl.department.visitor.ATMVisitor;
import com.lwerl.department.visitor.BalanceVisitor;
import com.lwerl.department.visitor.InitVisitor;

import java.util.List;

public class ATMDepartment {

    private List<ATMInfo> atmInfoList;

    public ATMDepartment(List<ATMInfo> atmInfoList) {
        this.atmInfoList = atmInfoList;
        initAllATM();
    }

    public List<ATMInfo> getAtmInfoList() {
        return atmInfoList;
    }

    private void visitAll(ATMVisitor visitor) {
        for (ATMInfo atmInfo: atmInfoList) {
            try {
                atmInfo.getAtm().accept(visitor, atmInfo);
            } catch (NotValidSecretKeyException e) {
                System.out.println("Приватная связь с банкоматом " + atmInfo.getAtm().toString() + " не установлена.");
            }
        }
    }

    public void initAllATM() {
        InitVisitor visitor = new InitVisitor();
        visitAll(visitor);
    }

    public int getAllBalance() {
        BalanceVisitor visitor = new BalanceVisitor();
        visitAll(visitor);
        return visitor.getContainer().get();
    }
}
