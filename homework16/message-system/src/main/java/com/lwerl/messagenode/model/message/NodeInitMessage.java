package com.lwerl.messagenode.model.message;

import com.lwerl.messagenode.MessageSystem;
import com.lwerl.messagenode.model.Address;

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
        addressee.sendMessage(new CommitInitMessage(getTo(), getFrom()));
    }
}
