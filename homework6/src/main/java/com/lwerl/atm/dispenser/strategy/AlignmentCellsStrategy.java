package com.lwerl.atm.dispenser.strategy;

import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;

import java.util.ArrayList;
import java.util.List;

public class AlignmentCellsStrategy implements DispenserStrategy {
    @Override
    public int[] getBanknoteCounts(int amount, List<Cell> cells) throws NotEnoughBanknotesException, NotValidAmountInputException {
        int tempAmount = amount;
        int[] banknoteCounts = new int[cells.size()];

        List<VirtualCell> virtualCells = new ArrayList<>();

        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            VirtualCell virtualCell = new VirtualCell();
            virtualCell.index = i;
            virtualCell.denomination = cell.getBanknoteDenomination();
            virtualCell.count = cell.getNumbersOfBanknotes();
            virtualCells.add(virtualCell);
        }

        boolean isSomeCellTouched = true;

        while (isSomeCellTouched) {
            isSomeCellTouched = false;
            virtualCells.sort((virtualCell1, virtualCell2) -> (virtualCell2.count - virtualCell1.count) == 0 ? virtualCell2.denomination - virtualCell1.denomination : virtualCell2.count - virtualCell1.count);

            for (VirtualCell virtualCell : virtualCells) {
                if (tempAmount / virtualCell.denomination > 0 && virtualCell.count > 0) {
                    tempAmount -= virtualCell.denomination;
                    virtualCell.count--;
                    banknoteCounts[virtualCell.index]++;
                    isSomeCellTouched = true;
                    break;
                }
            }
        }

        if (tempAmount > 0 ) {
            return new MinimalBanknotesStrategy().getBanknoteCounts(amount, cells);
        } else {
            return banknoteCounts;
        }
    }

    private static class VirtualCell {
        int denomination;
        int count;
        int index;
    }
}
