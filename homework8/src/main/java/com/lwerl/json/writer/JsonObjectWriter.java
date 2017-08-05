package com.lwerl.json.writer;

import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lwerl.json.helper.BuilderHelper.fillArrayBuilder;
import static com.lwerl.json.helper.BuilderHelper.fillObjectBuilder;
import static com.lwerl.json.helper.TypeHelper.*;

/**
 * Created by lWeRl on 05.08.2017.
 */
public class JsonObjectWriter {

    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    public String toJson(Object object) {

        if (isSimpleObject(object)) {
            if (isNullObject(object)) {
                return "null";
            } else if (isStringObject(object)) {
                return "\"" + object.toString() + "\"";
            } else if (isDateObject(object)) {
                return "\"" + dateFormat.format((Date) object) + "\"";
            } else {
                return object.toString();
            }
        }

        JsonStructure structure;

        if (isArrayObject(object)) {
            structure = fillArrayBuilder(Json.createArrayBuilder(), object, dateFormat).build();
        } else {
            structure = fillObjectBuilder(Json.createObjectBuilder(), object, dateFormat).build();
        }

        return toJsonString(structure);
    }

    private String toJsonString(JsonStructure structure) {
        String result = "";
        try (StringWriter sw = new StringWriter(); JsonWriter jw = Json.createWriter(sw)) {
            jw.write(structure);
            result = sw.toString();
        } catch (IOException e) {
            //Is never happen
        }
        return result;
    }

    public void setDatePattern(String s) {
        dateFormat.applyPattern(s);
    }
}
