package com.lwerl.messagesystem.util;

import com.google.gson.*;
import com.lwerl.messagesystem.model.Address;

import java.lang.reflect.Type;

/**
 * Created by lWeRl on 24.10.2017.
 */
public class DestinationGsonTypeAdapter implements JsonDeserializer<Address> {

    private static final String INSTANCE  = "INSTANCE";

    @Override
    public Address deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject message = json.getAsJsonObject();
            if (message.has(INSTANCE)) {
                Long id = message
                        .getAsJsonObject(INSTANCE)
                        .getAsJsonObject("to")
                        .getAsJsonPrimitive("id")
                        .getAsLong();
                return new Address(id);
            }
        }
        return null;
    }
}
