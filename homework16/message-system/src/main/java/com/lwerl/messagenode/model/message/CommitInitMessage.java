package com.lwerl.messagenode.model.message;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.MessageSystemHead;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class CommitInitMessage extends Message<MessageSystemHead> {

    public CommitInitMessage(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(MessageSystemHead addressee) {
        //TODO add new node to socket routing
    }
}
