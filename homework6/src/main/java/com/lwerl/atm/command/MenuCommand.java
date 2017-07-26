package com.lwerl.atm.command;

import com.lwerl.atm.keyboard.Keyboard;
import com.lwerl.atm.monitor.Monitor;

import java.util.Map;


public class MenuCommand implements Command {

    public static final String NAME = "getMenu";

    private Monitor monitor;
    private Map<String, Command> commandMap;
    private Keyboard keyboard;

    public MenuCommand(Monitor monitor, Map<String, Command> commandMap, Keyboard keyboard) {
        this.monitor = monitor;
        this.commandMap = commandMap;
        this.keyboard = keyboard;
    }

    @Override
    public void execute() {
        monitor.printMessage("Команды: ");
        commandMap.keySet().forEach(monitor::printMessage);
        String commandName = keyboard.inputCommand();
        Command command = commandMap.get(commandName);
        if (command == null) {
            monitor.printMessage("Введена не валиндая команда!");
        } else {
            command.execute();
        }
    }
}
