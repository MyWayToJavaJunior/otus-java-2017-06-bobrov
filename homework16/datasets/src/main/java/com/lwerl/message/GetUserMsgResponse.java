package com.lwerl.message;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.web.FrontendService;

/**
 * Created by lWeRl on 30.09.2017.
 */
public class GetUserMsgResponse extends Message<FrontendService> {

    private final UserDataSet user;
    private final Integer socketId;

    public GetUserMsgResponse(Address from, Address to, UserDataSet user, Integer  socketId) {
        super(from, to);
        this.user = user;
        this.socketId = socketId;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.proceedResponseUserById(user, socketId);
    }
}
