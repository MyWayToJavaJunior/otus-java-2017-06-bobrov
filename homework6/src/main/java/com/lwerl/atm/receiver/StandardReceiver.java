package com.lwerl.atm.receiver;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.exception.NoSuchBanknoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class StandardReceiver implements Receiver {

    private Set<Banknote> banknotes;

    public StandardReceiver(Set<Banknote> banknotes) {
        this.banknotes = banknotes;
    }

    @Override
    public List<Banknote> takeMoney() {

        List<Banknote> result = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        try {
            String input = scanner.nextLine();
            String[] banknotesInput = input.split(" ");
            for (String denominationString : banknotesInput) {
                final int denomination = Integer.parseInt(denominationString);
                Banknote banknote = banknotes.stream().filter(standard -> standard.getDenomination() == denomination).findFirst().orElseThrow(NoSuchBanknoteException::new);
                result.add(banknote);
            }
        } catch (Exception e) {
            result.clear();
        }
        return result;
    }
}
