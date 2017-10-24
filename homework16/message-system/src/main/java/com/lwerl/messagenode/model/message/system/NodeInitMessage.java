package com.lwerl.messagenode.model.message.system;

import com.lwerl.messagenode.MessageSystem;
import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.message.Message;
import com.lwerl.messagenode.model.message.system.CommitInitMessage;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class NodeInitMessage extends Message<MessageSystem> {

    public NodeInitMessage(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(MessageSystem addressee) {
        addressee.setAddress(getTo());
        addressee.setHeadAddress(getFrom());
        addressee.createWorker(addressee);
        addressee.sendMessage(new CommitInitMessage(getTo(), getFrom()));
    }
}
