package com.lwerl.testframework.util;

public class Timer {
    private static long startTime = -1;
    private static String TIMER_NOT_STARTED = "Таймер не запушен.";

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static long next() {
        if (startTime >= 0) {
            long lapResult = System.currentTimeMillis() - startTime;
            start();
            return lapResult;
        } else {
            System.out.println(TIMER_NOT_STARTED);
            return -1;
        }
    }

    public static long stop() {
        if (startTime >= 0) {
            long lapResult = System.currentTimeMillis() - startTime;
            startTime = -1;
            return lapResult;
        } else {
            System.out.println(TIMER_NOT_STARTED);
            return -1;
        }
    }
}
