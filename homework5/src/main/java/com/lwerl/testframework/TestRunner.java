package com.lwerl.testframework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class TestRunner {

    private final static String JAR_FILE_EXTENSION = ".jar";
    private final static String CLASS_FILE_EXTENSION = ".class";
    private final static String PACKAGE_SEPARATOR = ".";
    private final static String PACKAGE_SEPARATOR_REGEXP = "\\.";
    private final static String EMPTY_STRING = "";
    private final static String CLASS_TEST_NAMES_SEPARATOR = ":";

    private final static String CLASS_NOT_LOAD_ERROR_MSG = "Класс %s не удалось загрузить.\n";

    private final static String CLASS_MISSED = "Тестовый класс %s пропущен: %s\n";
    private final static String DEFAULT_CONSTRUCTOR_MISSED = "не имеет конструктора по умолчанию.";
    private final static String BEFORE_AFTER_METHODS_SIGNATURE = "методы помеченные аннотациями @Before/@After не должны содержать параметров в своей сигнатуре.";

    private final static String TEST_PREFIX = "Тест";
    private final static String TEST_DONE = TEST_PREFIX + " %s пройден.\n";
    private final static String TEST_FAILED = TEST_PREFIX + " %s провален.\n";
    private final static String TEST_FAILED_WITH_CAUSE = TEST_PREFIX + " %s провален: %s.\n";
    private final static String TEST_MISSED = TEST_PREFIX + " %s пропущен: тестовый метод не должен содержать параметров в своей сигнатуре.\n";
    private final static String EXPECTED_EXCEPTION = "ожидаемое исключение ";


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
        } catch (IOException | URISyntaxException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Set<ClassResult> getTestResults() {
        return testResults;
    }

    public void run() {
        preprocessor();
        test();
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
                    String testName = clazz.getName() + CLASS_TEST_NAMES_SEPARATOR + test.getName();

                    try {

                        Timer.start();
                        test.invoke(instance);

                        if (exceptionClass.equals(Test.Empty.class)) {
                            System.out.printf(TEST_DONE, testName);
                            methodResult = classResult.new MethodResult(test, true, Timer.stop(), EMPTY_STRING, null);
                        } else {
                            printFailMessage(testName, EXPECTED_EXCEPTION + exceptionClass.getName());
                            methodResult = classResult.new MethodResult(test, false, Timer.stop(), EXPECTED_EXCEPTION, null);
                        }

                    } catch (InvocationTargetException e) {

                        Throwable t = e.getCause();

                        if (exceptionClass.isInstance(t)) {
                            System.out.printf(TEST_DONE, testName);
                            methodResult = classResult.new MethodResult(test, true, Timer.stop(), EMPTY_STRING, null);
                        } else if (t instanceof AssertionError) {
                            printFailMessage(testName, t.getMessage());
                            methodResult = classResult.new MethodResult(test, false, Timer.stop(), t.getMessage(), t);
                        } else {
                            printFailMessage(testName, t.getClass().getName());
                            methodResult = classResult.new MethodResult(test, false, Timer.stop(), t.getClass().getName(), t);
                            t.printStackTrace(System.out);
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.printf(TEST_MISSED, testName);
                        methodResult = classResult.new MethodResult(test, false, Timer.stop(), TEST_MISSED, e);
                    }

                    // Выполняем все методы аннотированные @After
                    for (Method after : afterMap.get(clazz)) {
                        after.invoke(instance);
                    }

                    classResult.getMethodResultSet().add(methodResult);
                }

            } catch (InstantiationException e) {
                System.out.printf(CLASS_MISSED, clazz.getName(), DEFAULT_CONSTRUCTOR_MISSED);
                classResult.fail(DEFAULT_CONSTRUCTOR_MISSED, e);
            } catch (IllegalArgumentException e) {
                System.out.printf(CLASS_MISSED, clazz.getName(), BEFORE_AFTER_METHODS_SIGNATURE);
                classResult.fail(BEFORE_AFTER_METHODS_SIGNATURE, e);
            } catch (InvocationTargetException e) {
                System.out.printf(CLASS_MISSED, clazz.getName(), e.getCause());
                e.getCause().printStackTrace(System.out);
                classResult.fail(e.getCause().getClass().getName(), e.getCause());
            } catch (IllegalAccessException e) {
                classResult.fail(e.getClass().getName(), e);
                e.printStackTrace();
            }

            testResults.add(classResult);
        }
    }

    private void preprocessor() {
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

            beforeMap.put(clazz, before);
            testMap.put(clazz, test);
            afterMap.put(clazz, after);
        }

        testResults.clear();
    }

    private void printFailMessage(String testName, String cause) {
        if (cause == null) {
            System.out.printf(TEST_FAILED, testName);
        } else {
            System.out.printf(TEST_FAILED_WITH_CAUSE, testName, cause);
        }
    }

    private void setClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {

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
                        System.err.printf(CLASS_NOT_LOAD_ERROR_MSG, className.replace(CLASS_FILE_EXTENSION, EMPTY_STRING));
                    }
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
