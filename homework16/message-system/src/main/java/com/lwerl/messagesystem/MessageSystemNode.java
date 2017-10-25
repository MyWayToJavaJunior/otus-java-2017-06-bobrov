package com.lwerl.messagesystem;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Addressee;
import com.lwerl.messagesystem.model.Message;
import com.lwerl.messagesystem.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lWeRl on 08.10.2017.
 */
public class MessageSystemNode implements Addressee, com.lwerl.messagesystem.model.MessageSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSystemNode.class);

    private static final int DEFAULT_STEP_TIME = 25;
    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap = new ConcurrentHashMap<>();
    private final Map<Long, Addressee> toAdditionMap = new ConcurrentHashMap<>();
    private final List<Address> localAddresses = new CopyOnWriteArrayList<>();
    private final LinkedBlockingQueue<Message> toHeadMessageQuery = new LinkedBlockingQueue<>();
    private final boolean isRemoteHead;

    //TODO immutable list
    private ConcurrentHashMap<String, List<Address>> addresseeClassAddressesMap = new ConcurrentHashMap<>();
    private Socket socket;
    private Address address;
    private Address headAddress;
    private final Object initBlock = new Object();

    public MessageSystemNode() {
        isRemoteHead = false;
    }

    public MessageSystemNode(String host, int port) {
        isRemoteHead = true;
        initWithRemoteHead(host, port);
    }

    Addressee getWaiting(Long waitingId) {
        Addressee addressee = toAdditionMap.get(waitingId);
        toAdditionMap.remove(waitingId);
        return addressee;
    }

    public void addAddressee(final Addressee addressee) {
        addAddresseeAs(addressee, addressee.getClass());

    }

    public void addAddresseeAs(final Addressee addressee, Class<?> clazz) {
        synchronized (initBlock) {
            if (clazz.isAssignableFrom(addressee.getClass())) {
                if (addressee.getAddress() != null) {
                    createWorker(addressee);
                    localAddresses.add(addressee.getAddress());
                } else if (isRemoteHead) {
                    Long waitingId = COUNTER.incrementAndGet();
                    toAdditionMap.put(waitingId, addressee);
                    sendMessage(new AddAddresseeRequestMessage(this.getAddress(), this.getHeadAddress(), waitingId, clazz.getName()));
                } else {
                    addressee.setAddress(new Address(COUNTER.incrementAndGet()));
                    createWorker(addressee);
                    localAddresses.add(addressee.getAddress());
                }
            } else {
                LOGGER.error("Add addressee error.");
            }
        }
    }

    @Override
    public void sendMessage(Message message) {
        synchronized (initBlock) {
            Address to = message.getTo();
            if (isRemoteHead) {
                if (localAddresses.contains(to)) {
                    messagesMap.get(to).add(message);
                } else {
                    toHeadMessageQuery.add(message);
                }
            } else {
                messagesMap.get(to).add(message);
            }
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    private Address getHeadAddress() {
        return headAddress;
    }

    void setHeadAddress(Address headAddress) {
        this.headAddress = headAddress;
    }

    @SuppressWarnings("all")
    void createWorker(Addressee target) {
        final ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();

        messagesMap.put(target.getAddress(), queue);
        localAddresses.add(target.getAddress());

        final Logger workerLogger = LoggerFactory.getLogger(target.getClass().getSimpleName() + "-" + target.getAddress().getId());

        new Thread(() -> {
            while (true) {

                while (!queue.isEmpty()) {
                    Message message = queue.poll();
                    message.exec(target);
                }
                // Sleep for throttling
                try {
                    Thread.sleep(MessageSystemNode.DEFAULT_STEP_TIME);
                } catch (InterruptedException e) {
                    workerLogger.error("", e);
                    break;
                }
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    private void initWithRemoteHead(String host, int port) {
        synchronized (initBlock) {
            try {
                socket = new Socket(host, port);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                Message message = MessageHelper.deserializeMessage(reader);
                message.exec(this);

                startIncomingHandler(reader);
                startOutcomingHandler(writer);

            } catch (Exception e) {
                LOGGER.error("Error during init.", e);
            }
        }
    }

    private void startIncomingHandler(final BufferedReader reader) {
        new Thread(() -> {
            while (true) {
                try {
                    Message message = MessageHelper.deserializeMessage(reader);
                    if (localAddresses.contains(message.getTo()) || message.getTo().equals(getAddress())) {
                        sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startOutcomingHandler(final BufferedWriter writer) {
        new Thread(() -> {
            while (true) {
                try {
                    Message message = toHeadMessageQuery.take();
                    String toReceive = MessageHelper.serializeMessage(message);
                    writer.write(toReceive);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Как защитить промежуточное состояние перзаписи карты для уже отданных объектов?
    // Нужна сихронизация по объекту карты притом ее методы тоже
    synchronized void setAddresseeClassAddressesMap(ConcurrentHashMap<String, List<Address>> addresseeClassAddressesMap) {
        this.addresseeClassAddressesMap.clear();
        this.addresseeClassAddressesMap.putAll(addresseeClassAddressesMap);
    }

    public synchronized ConcurrentMap<String, List<Address>> getAddresseeClassAddressesMap() {
        return addresseeClassAddressesMap;
    }
}
