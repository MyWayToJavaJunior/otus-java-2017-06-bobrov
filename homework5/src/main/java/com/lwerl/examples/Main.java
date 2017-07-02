package com.lwerl.examples;

import com.lwerl.examples.fail.FailTest;
import com.lwerl.testframework.TestRunner;

public class Main {
    public static void main(String[] args) {
        new TestRunner("com.lwerl.examples.done");
        System.out.println();
        new TestRunner(FailTest.class);
        System.out.println();
        new TestRunner("");
    }
}
