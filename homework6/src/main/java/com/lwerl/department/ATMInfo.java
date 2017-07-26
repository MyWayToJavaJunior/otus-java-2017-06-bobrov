package com.lwerl.department;

import com.lwerl.atm.ATM;

public class ATMInfo {
    private int initialCount;
    private String secretKey;
    private ATM atm;

    public ATMInfo(int initialCount, String secretKey) {
        this.initialCount = initialCount;
        this.secretKey = secretKey;
        this.atm = new ATM(secretKey);
    }

    public int getInitialCount() {
        return initialCount;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public ATM getAtm() {
        return atm;
    }
}
