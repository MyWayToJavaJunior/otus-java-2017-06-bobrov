package com.lwerl.testframework.constant;

final public class Causes {

    //Class
    public final static String DEFAULT_CONSTRUCTOR_MISSED = "не имеет конструктора по умолчанию";
    public final static String BEFORE_AFTER_METHODS_SIGNATURE = "методы помеченные аннотациями @Before/@After не должны содержать параметров в своей сигнатуре";

    //Method
    public final static String TEST_METHOD_SIGNATURE = "тестовый метод не должен содержать параметров в своей сигнатуре";
    public final static String EXPECTED_EXCEPTION = "ожидаемое исключение %s";

    private Causes() {
    }
}
