package com.lwerl.start;

import com.lwerl.messagesystem.MessageSystemHead;

/**
 * Created by lWeRl on 02.10.2017.
 */
public class HeadStarter {
    public static void main(String[] args) {
        MessageSystemHead head = new MessageSystemHead(6666);
        head.start();
    }
}
