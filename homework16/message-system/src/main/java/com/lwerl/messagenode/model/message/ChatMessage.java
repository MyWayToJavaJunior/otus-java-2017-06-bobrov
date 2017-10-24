package com.lwerl.messagenode.model.message;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.message.Message;
import com.lwerl.messagenode.model.message.SimpleClient;

/**
 * Created by lWeRl on 24.10.2017.
 */
public class ChatMessage extends Message<SimpleClient> {

    private String text;

    public ChatMessage(Address from, Address to, String text) {
        super(from, to);
        this.text = text;
    }

    @Override
    public void exec(SimpleClient addressee) {
        addressee.printMessage(text);
    }
}
