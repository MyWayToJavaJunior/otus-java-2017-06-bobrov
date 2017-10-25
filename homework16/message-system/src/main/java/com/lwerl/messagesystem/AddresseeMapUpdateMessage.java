package com.lwerl.messagesystem;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lWeRl on 23.10.2017.
 */
public class AddresseeMapUpdateMessage extends Message<MessageSystemNode> {

    private final ConcurrentHashMap<String, List<Address>> addresseeClassAddressesMap;

    AddresseeMapUpdateMessage(Address from, Address to, ConcurrentHashMap<String, List<Address>> addresseeClassAddressesMap) {
        super(from, to);
        this.addresseeClassAddressesMap = addresseeClassAddressesMap;
    }

    @Override
    public void exec(MessageSystemNode addressee) {
        addressee.setAddresseeClassAddressesMap(addresseeClassAddressesMap);
    }

    public ConcurrentHashMap<String, List<Address>> getAddresseeClassAddressesMap() {
        return addresseeClassAddressesMap;
    }
}
