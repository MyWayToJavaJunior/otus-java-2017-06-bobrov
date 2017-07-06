package com.lwerl.testframework;

import com.lwerl.testframework.util.ResultPrinter;

public class Main {

    private Main() {
    }

    public static void test(Class<?>... classes) {
        TestRunner testRunner = new TestRunner(classes);
        testRunner.run();
        ResultPrinter.print(testRunner.getClassResultList(), false);
    }

    public static void test(String packageName) {
        TestRunner testRunner = new TestRunner(packageName);
        testRunner.run();
        ResultPrinter.print(testRunner.getClassResultList(), false);
    }

}
