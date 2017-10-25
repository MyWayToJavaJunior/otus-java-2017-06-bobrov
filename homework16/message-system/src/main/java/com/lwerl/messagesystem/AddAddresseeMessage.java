package com.lwerl.messagesystem;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Addressee;
import com.lwerl.messagesystem.model.Message;

/**
 * Created by lWeRl on 22.10.2017.
 */
public class AddAddresseeMessage extends Message<MessageSystemNode> {

    private Long waitingId;
    private Address establishedAddress;

    AddAddresseeMessage(Address from, Address to, Long waitingId, Address establishedAddress) {
        super(from, to);
        this.waitingId = waitingId;
        this.establishedAddress = establishedAddress;
    }

    @Override
    public void exec(MessageSystemNode addressee) {
        Addressee target = addressee.getWaiting(waitingId);
        target.setAddress(establishedAddress);
        addressee.createWorker(target);
    }
}
