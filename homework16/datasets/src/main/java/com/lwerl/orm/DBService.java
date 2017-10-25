package com.lwerl.orm;

import com.lwerl.messagesystem.model.Addressee;
import com.lwerl.messagesystem.model.MessageSystemHolder;
import com.lwerl.orm.model.UserDataSet;

public interface DBService extends Addressee, MessageSystemHolder {

    UserDataSet getUser(Long id);


}
