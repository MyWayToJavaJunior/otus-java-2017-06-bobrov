package com.lwerl.simpleclient;

import com.lwerl.messagenode.MessageSystem;
import com.lwerl.messagenode.model.message.ChatMessage;
import com.lwerl.messagenode.model.message.SimpleClient;

import java.util.Scanner;

/**
 * Created by lWeRl on 24.10.2017.
 */
public class Start {
    public static void main(String[] args) {
        MessageSystem messageSystem = new MessageSystem("127.0.0.1", 6666);
        try {
            Thread.sleep(100);

            SimpleClient sc = new SimpleClient();
            messageSystem.addAddressee(sc);

            Thread.sleep(1000);

            messageSystem.getAddressAddresseeClassMap().forEach((address, s)
                    -> System.out.println("Address id:" + address.getId() + ", class:" + s));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String text = scanner.nextLine();
                messageSystem.getAddressAddresseeClassMap().forEach((address, s) -> {
                    if (s.equals(sc.getClass().getName())) {
                        messageSystem.sendMessage(new ChatMessage(sc.getAddress(), address, text));
                    }
                });
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
