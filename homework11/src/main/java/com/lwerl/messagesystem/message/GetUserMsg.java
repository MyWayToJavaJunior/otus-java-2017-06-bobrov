package com.lwerl.messagesystem.message;

import com.lwerl.messagesystem.Address;
import com.lwerl.messagesystem.MessageSystem;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.orm.service.DBService;

/**
 * Created by lWeRl on 30.09.2017.
 */
public class GetUserMsg extends MsgToDB {

    private final Long userId;
    private final Integer socketId;
    private final MessageSystem messageSystem;

    public GetUserMsg(Address from, Address to, Long userId, Integer socketId, MessageSystem messageSystem) {
        super(from, to);
        this.userId = userId;
        this.socketId = socketId;
        this.messageSystem = messageSystem;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet user = dbService.getUser(userId);
        messageSystem.sendMessage(
                new GetUserMsgResponse(
                        getTo(),
                        getFrom(),
                        user,
                        socketId
                )
        );
    }
}
