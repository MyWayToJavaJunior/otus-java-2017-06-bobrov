package com.lwerl.atm.monitor;

public class StandardMonitor implements Monitor {
    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
