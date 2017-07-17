package com.lwerl.atm.banknote;

/**
 * Created by lWeRl on 15.07.2017.
 */
public enum StandardBanknote implements Banknote {

    // Заключим контракт: номинал банкноты перечисляется от большего к меньшему.
    FIFTY(50),
    TWENTY_FIVE(25),
    TEN(10),
    FIVE(5),
    ONE(1);

    private int denomination;

    StandardBanknote(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public int getDenomination() {
        return denomination;
    }
}
