package com.lwerl.web;

import com.lwerl.messagesystem.model.Addressee;
import com.lwerl.messagesystem.model.MessageSystem;
import com.lwerl.messagesystem.model.MessageSystemHolder;
import com.lwerl.orm.model.UserDataSet;

/**
 * Created by lWeRl on 30.09.2017.
 */
public interface FrontendService extends Addressee, MessageSystemHolder {
    void proceedRequestUserById(Long id, Integer socketId);
    void proceedResponseUserById(UserDataSet user, Integer socketId);
}
