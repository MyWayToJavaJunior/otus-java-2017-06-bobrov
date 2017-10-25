package com.lwerl.message;


import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Message;
import com.lwerl.orm.DBService;
import com.lwerl.orm.model.UserDataSet;

/**
 * Created by lWeRl on 30.09.2017.
 */
public class GetUserMsg extends Message<DBService> {

    private final Long userId;
    private final Integer socketId;

    public GetUserMsg(Address from, Address to, Long userId, Integer socketId) {
        super(from, to);
        this.userId = userId;
        this.socketId = socketId;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet user = dbService.getUser(userId);
        dbService.getMessageSystem()
                .sendMessage(new GetUserMsgResponse(getTo(), getFrom(), user, socketId));
    }
}
