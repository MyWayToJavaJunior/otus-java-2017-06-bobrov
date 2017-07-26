package com.lwerl.atm.cell;

import com.lwerl.atm.banknote.Banknote;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardCell implements Cell {

    private Banknote banknote;
    private int count;

    public StandardCell(Banknote banknote, int count) {
        this.banknote = banknote;
        this.count = count;
    }

    @Override
    public List<Banknote> getBanknotesFromCell(int count) {
        int result;
        if (count < this.count) {
            result = count;
            this.count -= count;
        } else {
            result = this.count;
            this.count = 0;
        }
        Stream<Banknote> stream = Stream.generate(() -> banknote).limit(result);
        return stream.collect(Collectors.toList());
    }

    @Override
    public int getNumbersOfBanknotes() {
        return count;
    }

    @Override
    public void setNumbersOfBanknotes(int count) {
        this.count = count;
    }

    @Override
    public int getBanknoteDenomination() {
        return banknote.getDenomination();
    }

    @Override
    public void increaseNumbersOfBanknotes(int count) {
        this.count += count;
    }
}
