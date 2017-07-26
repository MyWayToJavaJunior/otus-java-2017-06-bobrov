package com.lwerl.atm.banknote;

public enum StandardBanknote implements Banknote {

    P5000(5000),
    P1000(1000),
    P500(500),
    P100(100);

    private int denomination;

    StandardBanknote(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public int getDenomination() {
        return denomination;
    }
}
