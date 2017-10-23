package com.lwerl.messagenode.model.message;


import com.google.gson.*;
import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;

import java.lang.reflect.Type;

public abstract class Message<T extends Addressee> implements JsonSerializer<Message>, JsonDeserializer<Message> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    private final Address from;
    private final Address to;

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract void exec(T addressee);

    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        if (src != null) {
            String className = src.getClass().getName();
            JsonObject target = new JsonObject();
            target.addProperty(CLASSNAME, className);
            target.add(INSTANCE, context.serialize(src));
            return target;
        } else {
            return JsonNull.INSTANCE;
        }
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject.get(INSTANCE), clazz);
    }
}
