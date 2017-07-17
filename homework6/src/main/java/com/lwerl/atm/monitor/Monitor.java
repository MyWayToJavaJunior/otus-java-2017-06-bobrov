package com.lwerl.atm.monitor;

import com.lwerl.atm.command.Command;

public interface Monitor {

    void printMessage(String message);

    Command inputCommand();
}
