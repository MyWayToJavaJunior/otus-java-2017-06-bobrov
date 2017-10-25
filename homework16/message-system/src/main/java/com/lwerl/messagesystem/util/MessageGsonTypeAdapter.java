package com.lwerl.messagesystem.util;

import com.google.gson.*;
import com.lwerl.messagesystem.model.Message;

import java.lang.reflect.Type;

/**
 * Created by lWeRl on 23.10.2017.
 */
public class MessageGsonTypeAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    private Gson simple = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        if (src != null) {
            String className = src.getClass().getName();
            JsonObject target = new JsonObject();
            target.addProperty(CLASSNAME, className);
            target.add(INSTANCE, simple.toJsonTree(src));
            return target;
        } else {
            return JsonNull.INSTANCE;
        }
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = prim.getAsString();

            Class<? extends Message> clazz;
            try {
                clazz = (Class<? extends Message>) Class.forName(className);
                JsonElement instance = jsonObject.get(INSTANCE);
                return simple.fromJson(instance, clazz);
            } catch (Exception e) {
                throw new JsonParseException(e.getMessage());
            }
        }
    }
}
