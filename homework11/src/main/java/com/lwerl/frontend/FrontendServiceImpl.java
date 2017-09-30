package com.lwerl.frontend;

import com.google.gson.Gson;
import com.lwerl.messagesystem.Address;
import com.lwerl.messagesystem.message.GetUserMsg;
import com.lwerl.messagesystem.message.Message;
import com.lwerl.messagesystem.MessageSystem;
import com.lwerl.orm.model.PhoneDataSet;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.orm.service.DBServiceImpl;
import com.lwerl.websocket.MessageWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lWeRl on 30.09.2017.
 */

@Service
public class FrontendServiceImpl implements FrontendService {

    private Address address;
    private Map<Integer, MessageWebSocket> users;
    private MessageSystem messageSystem;

    @Autowired
    public FrontendServiceImpl(Map<Integer, MessageWebSocket> users, MessageSystem messageSystem) {
        this.users = users;
        this.messageSystem = messageSystem;
        this.address = new Address();
        this.messageSystem.addAddressee(this);
    }

    @Override
    public void proceedRequestUserById(Long id, Integer socketId) {
        Message msg = new GetUserMsg(
                getAddress(),
                messageSystem.getAddress(DBServiceImpl.class),
                id,
                socketId,
                messageSystem
        );
        messageSystem.sendMessage(msg);
    }

    @Override
    public void proceedResponseUserById(UserDataSet user, Integer socketId) {

        for (PhoneDataSet phone : user.getPhones()) {
            phone.setUser(null);
        }
        user.getAddress().setUser(null);

        users.get(socketId).sendMessage(new Gson().toJson(user));
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
