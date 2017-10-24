package com.lwerl.messagenode.model.message.system;

import com.lwerl.messagenode.MessageSystemHead;
import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.message.Message;

/**
 * Created by lWeRl on 22.10.2017.
 */
public class AddAddresseeRequestMessage extends Message<MessageSystemHead> {
    private final Long waitingId;
    private final String fullClassName;

    public AddAddresseeRequestMessage(Address from, Address to, Long waitingId, String fullClassName) {
        super(from, to);
        this.waitingId = waitingId;
        this.fullClassName = fullClassName;
    }

    @Override
    public void exec(MessageSystemHead addressee) {
        addressee.addRemoteAddressee(this);
    }

    public Long getWaitingId() {
        return waitingId;
    }

    public String getFullClassName() {
        return fullClassName;
    }
}
