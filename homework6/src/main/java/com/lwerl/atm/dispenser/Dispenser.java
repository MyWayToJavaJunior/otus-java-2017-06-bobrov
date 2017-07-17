package com.lwerl.atm.dispenser;

import com.lwerl.atm.banknote.Banknote;

import java.util.List;

public interface Dispenser {
    List<Banknote> giveCash(int... amountInfo);
}
