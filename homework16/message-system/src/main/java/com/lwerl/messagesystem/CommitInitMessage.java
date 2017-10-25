package com.lwerl.messagesystem;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.MessageSystemHead;
import com.lwerl.messagesystem.model.Message;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class CommitInitMessage extends Message<MessageSystemHead> {

    CommitInitMessage(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(MessageSystemHead addressee) {
        addressee.finishNodeRegistration(getFrom());
    }
}
