package com.lwerl.atm.dispenser;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.dispenser.strategy.DispenserStrategy;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;

import java.util.List;

public interface Dispenser {
    List<Banknote> giveCash(int amount) throws NotEnoughBanknotesException, NotValidAmountInputException;

    void setStrategy(DispenserStrategy strategy);
}
