package com.lwerl.testframework;

import com.lwerl.testframework.annotation.After;
import com.lwerl.testframework.annotation.Before;
import com.lwerl.testframework.annotation.Test;
import com.lwerl.testframework.util.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import com.lwerl.testframework.util.Timer;

import static com.lwerl.testframework.constant.Causes.*;
import static com.lwerl.testframework.constant.Literals.*;
import static com.lwerl.testframework.constant.Messages.*;


public class TestRunner {

    private Set<Class<?>> classes;
    private Set<ClassResult> testResults = new LinkedHashSet<>();
    private Map<Class<?>, Set<Method>> beforeMap = new HashMap<>();
    private Map<Class<?>, Set<Method>> testMap = new HashMap<>();
    private Map<Class<?>, Set<Method>> afterMap = new HashMap<>();

    public TestRunner(Class<?>... classes) {
        this.classes = new LinkedHashSet<>(Arrays.asList(classes));
    }

    public TestRunner(String packageName) {
        try {
            setClasses(packageName);
        } catch (IOException | URISyntaxException e) {
            System.out.printf(WRONG_PACKAGE_NAME_ERROR, e.getMessage());
        }
    }

    public Set<ClassResult> getTestResults() {
        return testResults;
    }

    public void run() {
        preprocessor();
        test();
    }

    public void print() {
        ResultPrinter.print(testResults, false);
    }

    private void test() {
        for (Class<?> clazz : classes) {

            ClassResult classResult = new ClassResult(clazz);

            try {

                for (Method test : testMap.get(clazz)) {
                    ClassResult.MethodResult methodResult;

                    Object instance = clazz.newInstance();

                    for (Method before : beforeMap.get(clazz)) {
                        before.invoke(instance);
                    }

                    Class exceptionClass = test.getAnnotation(Test.class).exception();

                    try {

                        Timer.start();
                        test.invoke(instance);

                        if (exceptionClass.equals(Test.Empty.class)) {
                            methodResult = classResult.new MethodResult(test, true, Timer.stop(), EMPTY_STRING, null);
                        } else {
                            String description = String.format(EXPECTED_EXCEPTION, exceptionClass.getName());
                            methodResult = classResult.new MethodResult(test, false, Timer.stop(), description, null);
                        }

                    } catch (InvocationTargetException e) {

                        Throwable t = e.getCause();
                        String exceptionName = t.getClass().getName();
                        String exceptionMessage = t.getMessage();

                        if (exceptionClass.isInstance(t)) {
                            methodResult = classResult.new MethodResult(test, true, Timer.stop(), EMPTY_STRING, null);
                        } else if (t instanceof AssertionError) {
                            methodResult = classResult.new MethodResult(test, false, Timer.stop(), exceptionMessage, t);
                        } else {
                            methodResult = classResult.new MethodResult(test, false, Timer.stop(), exceptionName, t);
                        }

                    } catch (IllegalArgumentException e) {
                        methodResult = classResult.new MethodResult(test, false, Timer.stop(), TEST_METHOD_SIGNATURE, e);
                    }

                    // Выполняем все методы аннотированные @After
                    for (Method after : afterMap.get(clazz)) {
                        after.invoke(instance);
                    }

                    classResult.getMethodResultSet().add(methodResult);
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

            testResults.add(classResult);
        }
    }

    private void preprocessor() {
        Set<Class<?>> excluded = new LinkedHashSet<>();
        for (Class<?> clazz : classes) {
            LinkedHashSet<Method> before = new LinkedHashSet<>();
            LinkedHashSet<Method> test = new LinkedHashSet<>();
            LinkedHashSet<Method> after = new LinkedHashSet<>();

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
            if (test.isEmpty()) {
                excluded.add(clazz);
            } else {
                beforeMap.put(clazz, before);
                testMap.put(clazz, test);
                afterMap.put(clazz, after);
            }
        }
        classes.removeAll(excluded);
        testResults.clear();
    }

    private void setClasses(String packageName) throws URISyntaxException, IOException {

        Set<Path> classpaths = new LinkedHashSet<>();
        String[] packages = packageName.split(PACKAGE_SEPARATOR_REGEXP);

        URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();

        for (URL url : system.getURLs()) {
            if (!url.toString().endsWith(JAR_FILE_EXTENSION)) {
                classpaths.add(Paths.get(url.toURI()));
            }
        }

        classes = new LinkedHashSet<>();
        for (Path classpath : classpaths) {
            Path dir = Paths.get(classpath.toString(), packages);
            ClassFileVisitor classFileVisitor = new ClassFileVisitor(classpath);
            Files.walkFileTree(dir, classFileVisitor);
        }
    }

    private class ClassFileVisitor extends SimpleFileVisitor<Path> {
        private Path classpath;

        ClassFileVisitor(Path classpath) {
            this.classpath = classpath;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (attrs.isRegularFile()) {
                String className = classpath.relativize(file)
                        .toString()
                        .replace(FileSystems.getDefault().getSeparator(), PACKAGE_SEPARATOR);

                if (className.endsWith(CLASS_FILE_EXTENSION)) {
                    try {
                        classes.add(Class.forName(className.replace(CLASS_FILE_EXTENSION, EMPTY_STRING)));
                    } catch (ClassNotFoundException e) {
                        System.out.printf(CLASS_NOT_LOAD_ERROR, className.replace(CLASS_FILE_EXTENSION, EMPTY_STRING));
                    }
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
