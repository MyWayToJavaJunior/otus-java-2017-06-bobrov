package com.lwerl.messagesystem.util;

import com.google.gson.*;
import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class MessageHelper {

    private MessageHelper() {
    }

    private static final Gson MESSAGE_GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Message.class, new MessageGsonTypeAdapter())
            .create();

    private static final Gson DESTINATION_GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Address.class, new DestinationGsonTypeAdapter())
            .create();

    public static Message deserializeMessage(BufferedReader reader) throws IOException {

        StringBuilder builder = new StringBuilder();
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            builder.append(line).append('\n');
        }

        return MESSAGE_GSON.fromJson(builder.toString(), Message.class);

    }

    public static Message deserializeMessage(String messageJson) {
        return MESSAGE_GSON.fromJson(messageJson, Message.class);
    }

    public static String serializeMessage(Message<?> message){

        String result = MESSAGE_GSON.toJson(message);
        result += "\n\n";
        return result;

    }

    public static String getMessageJson(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;

        while (!(line = reader.readLine()).isEmpty()) {
            builder.append(line).append('\n');
        }

        builder.append('\n');

        return builder.toString();
    }

    public static Address getDestination(String messageJson) {
        return DESTINATION_GSON.fromJson(messageJson, Address.class);
    }
}
