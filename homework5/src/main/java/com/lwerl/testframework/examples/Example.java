package com.lwerl.testframework.examples;

import com.lwerl.testframework.TestRunner;

public class Example {
    public static void main(String[] args) {
        TestRunner done = new TestRunner("com.lwerl.testframework");
        done.run();
        done.print();
    }
}
