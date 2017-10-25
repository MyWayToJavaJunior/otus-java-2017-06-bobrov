package com.lwerl.db;

import com.lwerl.messagesystem.MessageSystemNode;
import com.lwerl.orm.DBService;

/**
 * Created by lWeRl on 25.10.2017.
 */
public class DbServiceStarter {

    void start() {
        MessageSystemNode messageSystem = new MessageSystemNode("127.0.0.1", 6666);
        DBService dbService = new DbServiceImpl(messageSystem);
        messageSystem.addAddresseeAs(dbService, DBService.class);
    }

    public static void main(String[] args) {
        new DbServiceStarter().start();
    }

}
