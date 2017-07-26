package com.lwerl.atm.cell;

import com.lwerl.atm.InitPartForTest;
import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.banknote.StandardBanknote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class StandardCellTest extends InitPartForTest{

    @Override
    public void before() {
        initCell(100);
    }

    @Test
    public void getNumbersOfBanknotes() throws Exception {
        Assert.assertTrue(cell.getNumbersOfBanknotes() == 100);
    }

    @Test
    public void getBanknotesFromCell() throws Exception {
        List<Banknote> banknotes = cell.getBanknotesFromCell(10);
        Assert.assertTrue(cell.getNumbersOfBanknotes() == 90);
        Assert.assertTrue(banknotes.size() == 10);
        List<Banknote> matchList= new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            matchList.add(banknote);
        }
        Assert.assertThat(banknotes, is(matchList));
    }

    @Test
    public void setNumbersOfBanknotes() throws Exception {
        cell.setNumbersOfBanknotes(20);
        Assert.assertTrue(cell.getNumbersOfBanknotes() == 20);
    }

    @Test
    public void getBanknoteDenomination() throws Exception {
        Assert.assertTrue(cell.getBanknoteDenomination() == banknote.getDenomination());
    }

    @Test
    public void increaseNumbersOfBanknotes() throws Exception {
        cell.increaseNumbersOfBanknotes(20);
        Assert.assertTrue(cell.getNumbersOfBanknotes() == 120);
    }

}