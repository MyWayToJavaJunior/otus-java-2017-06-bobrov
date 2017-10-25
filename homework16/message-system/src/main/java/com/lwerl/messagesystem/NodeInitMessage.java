package com.lwerl.messagesystem;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class NodeInitMessage extends Message<MessageSystemNode> {

    NodeInitMessage(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(MessageSystemNode addressee) {
        addressee.setAddress(getTo());
        addressee.setHeadAddress(getFrom());
        addressee.createWorker(addressee);
        addressee.sendMessage(new CommitInitMessage(getTo(), getFrom()));
    }
}
