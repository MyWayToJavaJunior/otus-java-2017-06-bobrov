package com.lwerl.frontend;

import com.lwerl.messagesystem.Addressee;
import com.lwerl.orm.model.UserDataSet;

/**
 * Created by lWeRl on 30.09.2017.
 */
public interface FrontendService extends Addressee {
    void proceedRequestUserById(Long id, Integer socketId);
    void proceedResponseUserById(UserDataSet user, Integer socketId);
}
