package com.lwerl.testframework.constant;

final public class Messages {


    //Error
    public final static String CLASS_NOT_LOAD_ERROR = "Класс %s не удалось загрузить.\n";
    public final static String WRONG_PACKAGE_NAME_ERROR = "Путь не найден или его не возможно преобразовать в URI: %s.\n";

    //Class
    public final static String CLASS_PREFIX = "Тестовый класс";
    public final static String CLASS_TITLE = CLASS_PREFIX + " %s:\n";
    public final static String CLASS_CONCLUSION_DONE = CLASS_PREFIX + " %s пройден: количество тестов %d.\n";
    public final static String CLASS_CONCLUSION_FAIL = CLASS_PREFIX + " %s провален: выполнено %d/%d.\n";
    public final static String CLASS_MISSED = CLASS_PREFIX + " %s пропущен: %s.\n";

    //Method
    public final static String TEST_PREFIX = "\tТест";
    public final static String TEST_DONE = TEST_PREFIX + " %s пройден. Время выполения: %dмс.\n";
    public final static String TEST_FAILED = TEST_PREFIX + " %s провален.\n";
    public final static String TEST_FAILED_WITH_CAUSE = TEST_PREFIX + " %s провален: %s.\n";

    private Messages() {
    }
}
