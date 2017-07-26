package com.lwerl.atm.banknote;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class StandardBanknoteTest {
    @Test
    public void getDenomination() throws Exception {
        Assert.assertTrue(StandardBanknote.P100.getDenomination() == 100);
        Assert.assertTrue(StandardBanknote.P500.getDenomination() == 500);
        Assert.assertTrue(StandardBanknote.P1000.getDenomination() == 1000);
        Assert.assertTrue(StandardBanknote.P5000.getDenomination() == 5000);
    }
}