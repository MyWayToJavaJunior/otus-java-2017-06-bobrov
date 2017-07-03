package com.lwerl.testframework.util;

import com.lwerl.testframework.ClassResult;

import java.util.Set;

import static com.lwerl.testframework.constant.Messages.*;

final public class ResultPrinter {

    private ResultPrinter() {
    }

    public static void print(Set<ClassResult> results, boolean withException) {
        for (ClassResult classResult : results) {
            String className = classResult.getTestClass().getName();
            if (classResult.isExecuted()) {
                System.out.printf(CLASS_TITLE, className);
                int testCount = classResult.getMethodResultSet().size();
                int passedCount = 0;
                for (ClassResult.MethodResult methodResult : classResult.getMethodResultSet()) {
                    String methodName = methodResult.getTestMethod().getName();
                    if (methodResult.isPassed()) {
                        passedCount++;
                        System.out.printf(TEST_DONE, methodName, methodResult.getDuration());
                    } else {
                        if (methodResult.getCauseDescription() == null) {
                            System.out.printf(TEST_FAILED, methodName);
                        } else {
                            System.out.printf(TEST_FAILED_WITH_CAUSE, methodName, methodResult.getCauseDescription());
                        }
                        if (withException && methodResult.getCause() != null) {
                            methodResult.getCause().printStackTrace(System.out);
                        }
                    }
                }
                if (passedCount == testCount) {
                    System.out.printf(CLASS_CONCLUSION_DONE, className, testCount);
                } else {
                    System.out.printf(CLASS_CONCLUSION_FAIL, className, passedCount, testCount);
                }

            } else {
                System.out.printf(CLASS_MISSED, className, classResult.getCauseDescription());
                if (withException && classResult.getCause() != null) {
                    classResult.getCause().printStackTrace(System.out);
                }
            }
            System.out.println();
        }
    }
}
