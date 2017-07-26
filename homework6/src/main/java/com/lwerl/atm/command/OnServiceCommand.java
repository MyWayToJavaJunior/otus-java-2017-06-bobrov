package com.lwerl.atm.command;

import com.lwerl.atm.monitor.Monitor;

public class OnServiceCommand implements Command {

    private Monitor monitor;

    public OnServiceCommand(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void execute() {
        monitor.printMessage("Банкомат на обслуживании!");
    }
}
