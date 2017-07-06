package com.lwerl.testframework.util;

import static com.lwerl.testframework.constant.Messages.STOPWATCH_NOT_STARTED;

public class StopWatch {
    private static long startTime = -1;


    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static long next() {
        if (startTime >= 0) {
            long lapResult = System.currentTimeMillis() - startTime;
            start();
            return lapResult;
        } else {
            System.out.println(STOPWATCH_NOT_STARTED);
            return -1;
        }
    }

    public static long stop() {
        if (startTime >= 0) {
            long lapResult = System.currentTimeMillis() - startTime;
            startTime = -1;
            return lapResult;
        } else {
            System.out.println(STOPWATCH_NOT_STARTED);
            return -1;
        }
    }
}
