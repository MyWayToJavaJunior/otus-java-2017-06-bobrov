package com.lwerl.atm.receiver;

import com.lwerl.atm.banknote.Banknote;

import java.util.List;

public interface Receiver {

    List<Banknote> takeMoney();
}
