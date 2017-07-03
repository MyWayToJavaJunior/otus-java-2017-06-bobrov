package com.lwerl.testframework.examples;

import com.lwerl.testframework.examples.fail.FailTest;
import com.lwerl.testframework.TestRunner;

public class Main {
    public static void main(String[] args) {
        TestRunner done = new TestRunner("com.lwerl.testframework.examples.done");
        done.run();
        done.print();
        done = new TestRunner(FailTest.class);
        done.run();
        done.print();
        done = new TestRunner("");
        done.run();
        done.print();
    }
}
