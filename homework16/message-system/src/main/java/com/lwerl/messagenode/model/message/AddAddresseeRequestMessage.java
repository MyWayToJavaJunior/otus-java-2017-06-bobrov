package com.lwerl.messagenode.model.message;

import com.lwerl.messagenode.MessageSystemHead;
import com.lwerl.messagenode.model.Address;

/**
 * Created by lWeRl on 22.10.2017.
 */
public class AddAddresseeRequestMessage extends Message<MessageSystemHead> {
    private final Long waitingId;

    public AddAddresseeRequestMessage(Address from, Address to, Long waitingId) {
        super(from, to);
        this.waitingId = waitingId;
    }

    @Override
    public void exec(MessageSystemHead addressee) {

    }

    public Long getWaitingId() {
        return waitingId;
    }
}
