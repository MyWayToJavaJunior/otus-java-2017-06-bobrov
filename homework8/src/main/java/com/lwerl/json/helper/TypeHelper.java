package com.lwerl.json.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

/**
 * Created by lWeRl on 05.08.2017.
 */
public class TypeHelper {

    private TypeHelper() {
    }

    public static boolean isNullObject(Object object) {
        return object == null;
    }

    public static boolean isDecimalNumberObject(Object object) {
        Class<?> clazz = object.getClass();
        return clazz == float.class
                || clazz == Float.class
                || clazz == double.class
                || clazz == Double.class
                || clazz == BigDecimal.class;
    }

    public static boolean isIntegerNumberObject(Object object) {
        Class<?> clazz = object.getClass();
        return clazz == byte.class
                || clazz == Byte.class
                || clazz == short.class
                || clazz == Short.class
                || clazz == int.class
                || clazz == Integer.class
                || clazz == long.class
                || clazz == Long.class
                || clazz == BigInteger.class;
    }

    public static boolean isBooleanObject(Object object) {
        Class<?> clazz = object.getClass();
        return clazz == boolean.class
                || clazz == Boolean.class;
    }

    public static boolean isStringObject(Object object) {
        return object instanceof CharSequence;
    }

    public static boolean isDateObject(Object object) {
        return object instanceof Date;
    }

    public static boolean isArrayObject(Object object) {
        return object instanceof Collection || object.getClass().isArray();
    }

    public static boolean isSimpleObject(Object object) {
        return isNullObject(object)
                || isStringObject(object)
                || isBooleanObject(object)
                || isDecimalNumberObject(object)
                || isIntegerNumberObject(object)
                || isDateObject(object);
    }
}
