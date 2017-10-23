package com.lwerl.messagenode.model.message;

import com.lwerl.messagenode.MessageSystem;
import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;

/**
 * Created by lWeRl on 22.10.2017.
 */
public class AddAddresseeMessage extends Message<MessageSystem> {

    private Long waitingId;
    private Address establishedAddress;

    public AddAddresseeMessage(Address from, Address to, Long waitingId, Address establishedAddress) {
        super(from, to);
        this.waitingId = waitingId;
        this.establishedAddress = establishedAddress;
    }

    @Override
    public void exec(MessageSystem addressee) {
        Addressee target = addressee.getWaiting(waitingId);
        target.setAddress(establishedAddress);
        addressee.createWorker(target);
    }
}
