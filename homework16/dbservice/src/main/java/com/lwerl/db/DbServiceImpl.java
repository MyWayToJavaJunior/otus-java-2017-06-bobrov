package com.lwerl.db;

import com.lwerl.messagesystem.MessageSystemNode;
import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.MessageSystem;
import com.lwerl.orm.DBService;
import com.lwerl.orm.model.AddressDataSet;
import com.lwerl.orm.model.UserDataSet;

/**
 * Created by lWeRl on 25.10.2017.
 */
public class DbServiceImpl implements DBService {

    private MessageSystemNode messageSystem;
    private Address address;

    public DbServiceImpl(MessageSystemNode messageSystem) {
        this.messageSystem = messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public UserDataSet getUser(Long id) {
        UserDataSet mock =  new UserDataSet();
        mock.setId(123);
        mock.setName("MOCK");
        mock.setAge(99);
        AddressDataSet addressMock = new AddressDataSet();
        addressMock.setAddress("hell");
        addressMock.setId(666);
        mock.setAddress(addressMock);
        return mock;
    }
}
