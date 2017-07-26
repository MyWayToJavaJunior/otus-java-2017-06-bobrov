package com.lwerl.atm.dispenser;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.dispenser.strategy.AlignmentCellsStrategy;
import com.lwerl.atm.dispenser.strategy.DispenserStrategy;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;

import java.util.ArrayList;
import java.util.List;

public class StandardDispenser implements Dispenser {
    private List<Cell> cells;
    private DispenserStrategy strategy = new AlignmentCellsStrategy();

    public StandardDispenser(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public void setStrategy(DispenserStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public List<Banknote> giveCash(int amount) throws NotEnoughBanknotesException, NotValidAmountInputException {
        int[] banknoteCount = strategy.getBanknoteCounts(amount, cells);

        List<Banknote> result = new ArrayList<>();
        for (int i = 0; i < banknoteCount.length; i++) {
            result.addAll(cells.get(i).getBanknotesFromCell(banknoteCount[i]));
        }
        return result;
    }
}
