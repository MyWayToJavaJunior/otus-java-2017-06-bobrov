package com.lwerl.simpleclient;

import com.lwerl.messagesystem.MessageSystemNode;
import com.lwerl.messagesystem.model.Address;

import java.util.List;
import java.util.Scanner;

/**
 * Created by lWeRl on 24.10.2017.
 */
public class ChatStarter {
    public static void main(String[] args) {
        MessageSystemNode messageSystem = new MessageSystemNode("127.0.0.1", 6666);
        try {
            Thread.sleep(100);

            SimpleClient sc = new SimpleClient();
            messageSystem.addAddressee(sc);

            Thread.sleep(1000);

            List<Address> addressList = messageSystem.getAddresseeClassAddressesMap().get(SimpleClient.class.getName());
            if(addressList != null) {
                addressList.forEach( address -> {
                    System.out.println("Address id:" + address.getId());
                });
            }

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String text = scanner.nextLine();
                addressList = messageSystem.getAddresseeClassAddressesMap().get(SimpleClient.class.getName());
                if(addressList != null) {
                    addressList.forEach(address -> {
                        messageSystem.sendMessage(new ChatMessage(sc.getAddress(), address, text));
                    });
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
