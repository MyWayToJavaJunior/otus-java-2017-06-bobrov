package com.lwerl.json.helper;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.lwerl.json.helper.TypeHelper.*;
import static java.util.Map.Entry;

/**
 * Created by lWeRl on 05.08.2017.
 */
public class BuilderHelper {
    private BuilderHelper() {
    }

    public static JsonArrayBuilder fillArrayBuilder(JsonArrayBuilder builder, Object array, SimpleDateFormat format) {
        if (array instanceof Collection) {
            Collection collection = (Collection) array;
            for (Object obj : collection) {
                addObjectToArrayBuilder(builder, obj, format);
            }
        } else {
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(array, i);
                addObjectToArrayBuilder(builder, obj, format);
            }
        }
        return builder;
    }


    @SuppressWarnings("unchecked")
    public static JsonObjectBuilder fillObjectBuilder(JsonObjectBuilder builder, Object object, SimpleDateFormat format) {
        if (object instanceof Map) {
            Map map = (Map) object;
            Set<Entry> entrySet = map.entrySet();
            for (Entry entry : entrySet) {
                addObjectToObjectBuilder(builder, entry.getKey().toString(), entry.getValue(), format);
            }
        } else {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                try {
                    Object value = field.get(object);
                    addObjectToObjectBuilder(builder, name, value, format);
                } catch (IllegalAccessException e) {
                    Logger.getAnonymousLogger().log(Level.WARNING, "Field " + name + " is skip!");
                }
            }
        }
        return builder;
    }

    public static void addObjectToArrayBuilder(JsonArrayBuilder builder, Object obj, SimpleDateFormat format) {
        if (isNullObject(obj)) {
            builder.addNull();
        } else if (isStringObject(obj)) {
            builder.add(obj.toString());
        } else if (isDecimalNumberObject(obj)) {
            builder.add(new BigDecimal(obj.toString()));
        } else if (isIntegerNumberObject(obj)) {
            builder.add(new BigInteger(obj.toString()));
        } else if (isBooleanObject(obj)) {
            builder.add((boolean) obj);
        } else if (isDateObject(obj)) {
            builder.add(format.format((Date) obj));
        } else if (isArrayObject(obj)) {
            builder.add(fillArrayBuilder(Json.createArrayBuilder(), obj, format));
        } else {
            builder.add(fillObjectBuilder(Json.createObjectBuilder(), obj, format));
        }
    }

    public static void addObjectToObjectBuilder(JsonObjectBuilder builder, String name, Object obj, SimpleDateFormat format) {
        if (isNullObject(obj)) {
            builder.addNull(name);
        } else if (isStringObject(obj)) {
            builder.add(name, obj.toString());
        } else if (isDecimalNumberObject(obj)) {
            builder.add(name, new BigDecimal(obj.toString()));
        } else if (isIntegerNumberObject(obj)) {
            builder.add(name, new BigInteger(obj.toString()));
        } else if (isBooleanObject(obj)) {
            builder.add(name, (boolean) obj);
        } else if (isDateObject(obj)) {
            builder.add(name, format.format((Date) obj));
        } else if (isArrayObject(obj)) {
            builder.add(name, fillArrayBuilder(Json.createArrayBuilder(), obj, format));
        } else {
            builder.add(name, fillObjectBuilder(Json.createObjectBuilder(), obj, format));
        }
    }
}
