package com.lwerl.messagesystem;

import com.lwerl.messagesystem.MessageSystemHead;
import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;

/**
 * Created by lWeRl on 22.10.2017.
 */
public class AddAddresseeRequestMessage extends Message<MessageSystemHead> {
    private final Long waitingId;
    private final String fullClassName;

    AddAddresseeRequestMessage(Address from, Address to, Long waitingId, String fullClassName) {
        super(from, to);
        this.waitingId = waitingId;
        this.fullClassName = fullClassName;
    }

    @Override
    public void exec(MessageSystemHead addressee) {
        addressee.addRemoteAddressee(this);
    }

    Long getWaitingId() {
        return waitingId;
    }

    String getFullClassName() {
        return fullClassName;
    }
}
