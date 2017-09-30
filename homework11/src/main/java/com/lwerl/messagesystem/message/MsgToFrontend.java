package com.lwerl.messagesystem.message;

import com.lwerl.frontend.FrontendService;
import com.lwerl.messagesystem.Address;
import com.lwerl.messagesystem.Addressee;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        }
    }

    public abstract void exec(FrontendService frontendService);
}