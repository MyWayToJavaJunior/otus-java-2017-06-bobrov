package com.lwerl.frontend;

import com.google.gson.Gson;
import com.lwerl.frontend.websocket.MessageWebSocket;
import com.lwerl.message.GetUserMsg;
import com.lwerl.messagesystem.MessageSystemNode;
import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;
import com.lwerl.messagesystem.model.MessageSystem;
import com.lwerl.orm.DBService;
import com.lwerl.orm.model.PhoneDataSet;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.web.FrontendService;

import java.util.List;
import java.util.Map;

/**
 * Created by lWeRl on 30.09.2017.
 */

public class FrontendServiceImpl implements FrontendService {

    private Address address;
    private Map<Integer, MessageWebSocket> users;
    private MessageSystemNode messageSystem;

    public FrontendServiceImpl(Map<Integer, MessageWebSocket> users, MessageSystemNode messageSystem) {
        this.users = users;
        this.messageSystem = messageSystem;
        this.messageSystem.addAddressee(this);
    }

    @Override
    public void proceedRequestUserById(Long id, Integer socketId) {
        List<Address> dbServices = messageSystem.getAddresseeClassAddressesMap().get(DBService.class.getName());
        if (dbServices != null && !dbServices.isEmpty()) {
            Message msg = new GetUserMsg(
                    getAddress(),
                    dbServices.get(0),
                    id,
                    socketId
            );
            messageSystem.sendMessage(msg);
        }
    }

    @Override
    public void proceedResponseUserById(UserDataSet user, Integer socketId) {

        if (user.getPhones() != null) {
            for (PhoneDataSet phone : user.getPhones()) {
                phone.setUser(null);
            }
        }
        user.getAddress().setUser(null);

        users.get(socketId).sendMessage(new Gson().toJson(user));
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
}
