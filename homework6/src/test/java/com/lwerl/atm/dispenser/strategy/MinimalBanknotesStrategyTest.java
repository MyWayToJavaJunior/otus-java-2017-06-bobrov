package com.lwerl.atm.dispenser.strategy;

import com.lwerl.atm.InitPartForTest;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinimalBanknotesStrategyTest extends InitPartForTest{

    @Override
    public void before() {
        initCells(5);

    }

    @Test
    public void getBanknoteCountsTest() throws Exception {
        MinimalBanknotesStrategy strategy = new MinimalBanknotesStrategy();
        int[] actual = strategy.getBanknoteCounts(6600, cells);
        int[] expected = {1, 1, 1, 1};
        Assert.assertArrayEquals(expected, actual);
        actual = strategy.getBanknoteCounts(10100, cells);
        expected = new int[] {2, 0, 0, 1};
        Assert.assertArrayEquals(expected, actual);
        actual = strategy.getBanknoteCounts(4900, cells);
        expected = new int[] {0, 4, 1, 4};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = NotEnoughBanknotesException.class)
    public void getBanknoteCountsNotEnoughBanknotesExceptionTest() throws Exception {
        MinimalBanknotesStrategy strategy = new MinimalBanknotesStrategy();
        strategy.getBanknoteCounts(100000, cells);
    }

    @Test(expected = NotValidAmountInputException.class)
    public void getBanknoteCountsNotValidAmountInputExceptionTest() throws Exception {
        MinimalBanknotesStrategy strategy = new MinimalBanknotesStrategy();
        strategy.getBanknoteCounts(10150, cells);
    }
}