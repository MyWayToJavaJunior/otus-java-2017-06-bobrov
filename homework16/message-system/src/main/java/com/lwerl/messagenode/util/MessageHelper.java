package com.lwerl.messagenode.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lwerl.messagenode.model.message.Message;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class MessageHelper {

    private MessageHelper() {
    }

    private static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Message.class, new MessageGsonTypeAdapter())
            .create();

    public static Message deserializeMessage(BufferedReader reader) throws IOException {

        StringBuilder builder = new StringBuilder();
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            builder.append(line).append('\n');
        }

        return gson.fromJson(builder.toString(), Message.class);

    }

    public static String serializeMessage(Message<?> message){

        String result = gson.toJson(message);
        result += "\n\n";
        return result;

    }
}
