package com.lwerl.messagesystem.message;

import com.lwerl.frontend.FrontendService;
import com.lwerl.messagesystem.Address;
import com.lwerl.orm.model.UserDataSet;

/**
 * Created by lWeRl on 30.09.2017.
 */
public class GetUserMsgResponse extends MsgToFrontend {

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
