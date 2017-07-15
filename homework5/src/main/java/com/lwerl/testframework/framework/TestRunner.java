package com.lwerl.testframework.framework;

import com.lwerl.testframework.annotation.After;
import com.lwerl.testframework.annotation.Before;
import com.lwerl.testframework.annotation.Test;
import com.lwerl.testframework.util.StopWatch;
import com.lwerl.testframework.util.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.lwerl.testframework.constant.Causes.*;
import static com.lwerl.testframework.constant.Literals.EMPTY_STRING;
import static com.lwerl.testframework.constant.Messages.WAS_ALREADY_STARTED;
import static com.lwerl.testframework.constant.Messages.WRONG_PACKAGE_NAME_ERROR;

public class TestRunner {

    private List<ClassInfo> classInfoList = new ArrayList<>();
    private List<ClassResult> classResultList = new ArrayList<>();
    private boolean wasStarted;

    public TestRunner(Class<?>... classes) {
        preprocessor(classes);
    }

    public TestRunner(String packageName) {
        try {
            preprocessor(Utils.getClassesInPackage(packageName).toArray(new Class<?>[0]));
        } catch (IOException | URISyntaxException e) {
            System.out.printf(WRONG_PACKAGE_NAME_ERROR, e.getMessage());
        }
    }

    public List<ClassInfo> getClassInfoList() {
        return classInfoList;
    }

    public List<ClassResult> getClassResultList() {
        return classResultList;
    }

    public void run() {

        if (wasStarted) {
            System.out.println(WAS_ALREADY_STARTED);
        } else {
            for (ClassInfo classInfo : classInfoList) {

                Class<?> clazz = classInfo.getClazz();
                ClassResult classResult = new ClassResult(clazz);

                try {

                    for (Method test : classInfo.getTestList()) {
                        Object instance = clazz.newInstance();

                        invokeBefore(classInfo, instance);
                        invokeTest(classResult, test, instance);
                        invokeAfter(classInfo, instance);

                    }

                } catch (InstantiationException e) {
                    classResult.fail(DEFAULT_CONSTRUCTOR_MISSED, e);
                } catch (IllegalArgumentException e) {
                    classResult.fail(BEFORE_AFTER_METHODS_SIGNATURE, e);
                } catch (InvocationTargetException e) {
                    classResult.fail(e.getCause().getClass().getName(), e.getCause());
                } catch (IllegalAccessException e) {
                    classResult.fail(e.getClass().getName(), e);
                }

                classResultList.add(classResult);
            }
            wasStarted = true;
        }
    }

    private void preprocessor(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            List<Method> before = new ArrayList<>();
            List<Method> test = new ArrayList<>();
            List<Method> after = new ArrayList<>();

            for (Method m : clazz.getMethods()) {
                if (m.getAnnotation(Before.class) != null) {
                    before.add(m);
                }
                if (m.getAnnotation(Test.class) != null) {
                    test.add(m);
                }
                if (m.getAnnotation(After.class) != null) {
                    after.add(m);
                }
            }
            if (!test.isEmpty()) {
                ClassInfo classInfo = new ClassInfo(clazz);
                classInfo.setBeforeList(before);
                classInfo.setTestList(test);
                classInfo.setAfterList(after);
                classInfoList.add(classInfo);
            }
        }
    }

    private void invokeBefore(ClassInfo classInfo, Object instance) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        for (Method before : classInfo.getBeforeList()) {
            before.invoke(instance);
        }
    }

    private void invokeAfter(ClassInfo classInfo, Object instance) throws InvocationTargetException, IllegalAccessException {
        for (Method after : classInfo.getAfterList()) {
            after.invoke(instance);
        }
    }

    private void invokeTest(ClassResult classResult, Method test, Object instance) {
        ClassResult.MethodResult methodResult;
        Class exceptionClass = test.getAnnotation(Test.class).exception();

        try {

            StopWatch.start();
            test.invoke(instance);

            if (exceptionClass.equals(Test.Empty.class)) {
                methodResult = classResult.new MethodResult(test, true, StopWatch.stop(), EMPTY_STRING, null);
            } else {
                String description = String.format(EXPECTED_EXCEPTION, exceptionClass.getName());
                methodResult = classResult.new MethodResult(test, false, StopWatch.stop(), description, null);
            }

        } catch (InvocationTargetException e) {

            Throwable t = e.getCause();
            String exceptionName = t.getClass().getName();
            String exceptionMessage = t.getMessage();

            if (exceptionClass.isInstance(t)) {
                methodResult = classResult.new MethodResult(test, true, StopWatch.stop(), EMPTY_STRING, null);
            } else if (t instanceof AssertionError) {
                methodResult = classResult.new MethodResult(test, false, StopWatch.stop(), exceptionMessage, t);
            } else {
                methodResult = classResult.new MethodResult(test, false, StopWatch.stop(), exceptionName, t);
            }

        } catch (IllegalArgumentException e) {
            methodResult = classResult.new MethodResult(test, false, StopWatch.stop(), TEST_METHOD_SIGNATURE, e);
        } catch (IllegalAccessException e) {
            methodResult = classResult.new MethodResult(test, false, StopWatch.stop(), e.getClass().getName(), e);
        }
        classResult.getMethodResultList().add(methodResult);
    }
}
