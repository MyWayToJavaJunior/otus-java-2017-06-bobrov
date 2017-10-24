package com.lwerl.messagenode.model.message.system;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.MessageSystemHead;
import com.lwerl.messagenode.model.message.Message;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class CommitInitMessage extends Message<MessageSystemHead> {

    public CommitInitMessage(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(MessageSystemHead addressee) {
        addressee.finishNodeRegistration(getFrom());
    }
}
