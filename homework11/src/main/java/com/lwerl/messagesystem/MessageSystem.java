package com.lwerl.messagesystem;

import com.lwerl.messagesystem.message.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public final class MessageSystem {
    private static final int DEFAULT_STEP_TIME = 25;

    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap = new HashMap<>();
    private final Map<Class<?>, Address> addressMap = new HashMap<>();

    public void addAddressee(final Addressee addressee) {

        final ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();

        messagesMap.put(addressee.getAddress(), queue);
        addressMap.put(addressee.getClass(), addressee.getAddress());

        new Thread(() -> {
            while (true) {

                while (!queue.isEmpty()) {
                    Message message = queue.poll();
                    message.exec(addressee);
                }
                // Sleep for throttling
                try {
                    Thread.sleep(MessageSystem.DEFAULT_STEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }

    public Address getAddress(Class<?> clazz) {
        return addressMap.get(clazz);
    }

}
