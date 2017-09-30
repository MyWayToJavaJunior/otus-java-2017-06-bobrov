package com.lwerl.messagesystem.message;

import com.lwerl.messagesystem.Address;
import com.lwerl.messagesystem.Addressee;
import com.lwerl.orm.service.DBService;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
