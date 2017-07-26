package com.lwerl.atm.dispenser.strategy;

import com.lwerl.atm.InitPartForTest;
import com.lwerl.atm.cell.Cell;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class AlignmentCellsStrategyTest extends InitPartForTest {

    @Override
    public void before() {
        initCells(5);
    }

    @Test
    public void getBanknoteCountsAlgorithmTest() throws Exception {
        AlignmentCellsStrategy strategy = new AlignmentCellsStrategy();

        int testCount = 10000;
        int delta = 10;

        int collisionCount = 0;
        for (int k = 0; k < testCount; k++) {
            initCells(1000);
            int[] temp;

            while (true) {
                try {
                    int amount = ThreadLocalRandom.current().nextInt(1, 401) * 100;
                    temp = strategy.getBanknoteCounts(amount, cells);
                    for (int i = 0; i < cells.size(); i++) {
                        cells.get(i).getBanknotesFromCell(temp[i]);
                    }
                } catch (Exception e) {
                    break;
                }
            }

            int[] actual = cells.stream().mapToInt(Cell::getNumbersOfBanknotes).toArray();
            Arrays.sort(actual);
            int currentDelta = actual[actual.length - 1] - actual[0];
            if (currentDelta > delta) collisionCount++;
        }

        float collisionPercent = (float) collisionCount / testCount * 100;
        System.out.println("Collision percent: " + collisionPercent + "%");
        Assert.assertTrue(collisionPercent < 5);
    }

    @Test
    public void getBanknoteCountsTest() throws Exception {
        AlignmentCellsStrategy strategy = new AlignmentCellsStrategy();
        int expected = 9700;
        int actual = 0;
        int[] banknoteCounts = strategy.getBanknoteCounts(expected, cells);
        for (int i = 0; i < cells.size(); i++) {
            actual += banknoteCounts[i] * cells.get(i).getBanknoteDenomination();
        }
        Assert.assertTrue(actual == expected);
    }

    @Test(expected = NotEnoughBanknotesException.class)
    public void getBanknoteCountsNotEnoughBanknotesExceptionTest() throws Exception {
        AlignmentCellsStrategy strategy = new AlignmentCellsStrategy();
        strategy.getBanknoteCounts(100000, cells);
    }

    @Test(expected = NotValidAmountInputException.class)
    public void getBanknoteCountsNotValidAmountInputExceptionTest() throws Exception {
        AlignmentCellsStrategy strategy = new AlignmentCellsStrategy();
        strategy.getBanknoteCounts(10150, cells);
    }
}