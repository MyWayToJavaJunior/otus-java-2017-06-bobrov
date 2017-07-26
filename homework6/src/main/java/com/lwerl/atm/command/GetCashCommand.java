package com.lwerl.atm.command;

import com.lwerl.atm.banknote.Banknote;
import com.lwerl.atm.dispenser.Dispenser;
import com.lwerl.atm.dispenser.strategy.AlignmentCellsStrategy;
import com.lwerl.atm.dispenser.strategy.MinimalBanknotesStrategy;
import com.lwerl.atm.exception.NotEnoughBanknotesException;
import com.lwerl.atm.exception.NotValidAmountInputException;
import com.lwerl.atm.exception.WrongAmountInputException;
import com.lwerl.atm.keyboard.Keyboard;
import com.lwerl.atm.monitor.Monitor;

import java.util.List;
import java.util.stream.Collectors;

public class GetCashCommand implements Command {

    public static final String NAME = "getCash";

    private Keyboard keyboard;
    private Monitor monitor;
    private Dispenser dispenser;

    public GetCashCommand(Keyboard keyboard, Monitor monitor, Dispenser dispenser) {
        this.keyboard = keyboard;
        this.monitor = monitor;
        this.dispenser = dispenser;
    }

    @Override
    public void execute() {
        monitor.printMessage("Сумма для снятия:");
        try {
            int amount = keyboard.inputNumber();
            monitor.printMessage("Крупными купюрами? (y/n)");
            String strategyAnswer = keyboard.inputCommand();
            if (strategyAnswer.equals("y")) {
                dispenser.setStrategy(new MinimalBanknotesStrategy());
            } else {
                dispenser.setStrategy(new AlignmentCellsStrategy());
            }
            List<Banknote> banknotes;
            banknotes = dispenser.giveCash(amount);
            String banknoteMessage = banknotes.stream().map(banknote -> String.valueOf(banknote.getDenomination())).collect(Collectors.joining(" "));
            monitor.printMessage("Выдано: " + banknoteMessage);
        } catch (NotEnoughBanknotesException notEnoughBanknotesException) {
            monitor.printMessage("В банкомате недостаточно денег!");
        } catch (WrongAmountInputException e) {
            monitor.printMessage("Введена не валидная сумма!");
        } catch (NotValidAmountInputException e) {
            monitor.printMessage("Данную сумму невозможно выдать!");
        }
    }
}
