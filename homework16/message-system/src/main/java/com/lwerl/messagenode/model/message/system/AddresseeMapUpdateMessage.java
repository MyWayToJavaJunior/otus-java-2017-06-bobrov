package com.lwerl.messagenode.model.message.system;

import com.lwerl.messagenode.MessageSystem;
import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.message.Message;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lWeRl on 23.10.2017.
 */
public class AddresseeMapUpdateMessage extends Message<MessageSystem> {

    private final ConcurrentHashMap<Address, String> addressAddresseeClassMap;

    public AddresseeMapUpdateMessage(Address from, Address to, ConcurrentHashMap<Address, String> addressAddresseeClassMap) {
        super(from, to);
        this.addressAddresseeClassMap = addressAddresseeClassMap;
    }

    @Override
    public void exec(MessageSystem addressee) {
        addressee.setAddressAddresseeClassMap(addressAddresseeClassMap);
    }

    public ConcurrentHashMap<Address, String> getAddressAddresseeClassMap() {
        return addressAddresseeClassMap;
    }
}
