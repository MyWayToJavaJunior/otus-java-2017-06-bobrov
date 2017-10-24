package com.lwerl.messagenode;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;
import com.lwerl.messagenode.model.message.system.AddAddresseeRequestMessage;
import com.lwerl.messagenode.model.message.Message;
import com.lwerl.messagenode.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lWeRl on 08.10.2017.
 */
public class MessageSystem implements Addressee {

    private static final Logger logger = LoggerFactory.getLogger(MessageSystem.class);

    private static final int DEFAULT_STEP_TIME = 25;
    private static final AtomicLong counter = new AtomicLong(0);

    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap = new ConcurrentHashMap<>();
    private final Map<Long, Addressee> toAdditionMap = new ConcurrentHashMap<>();
    private final List<Address> localAddresses = new CopyOnWriteArrayList<>();
    private final LinkedBlockingQueue<Message> toHeadMessageQuery = new LinkedBlockingQueue<>();
    private final boolean isRemoteHead;

    private ConcurrentHashMap<Address, String> addressAddresseeClassMap = new ConcurrentHashMap<>();
    private Socket socket;
    private Address address;
    private Address headAddress;

    public MessageSystem() {
        isRemoteHead = false;
    }

    public MessageSystem(String host, int port) {
        isRemoteHead = true;
        initWithRemoteHead(host, port);
    }

    public Addressee getWaiting(Long waitingId) {
        return toAdditionMap.get(waitingId);
    }

    public void addAddressee(final Addressee addressee) {
        if (addressee.getAddress() != null) {
            createWorker(addressee);
            localAddresses.add(addressee.getAddress());
        } else if (isRemoteHead) {
            Long waitingId = counter.incrementAndGet();
            toAdditionMap.put(waitingId, addressee);
            sendMessage(new AddAddresseeRequestMessage(this.getAddress(), this.getHeadAddress(), waitingId, addressee.getClass().getName()));
        } else {
            addressee.setAddress(new Address(counter.incrementAndGet()));
            createWorker(addressee);
            localAddresses.add(addressee.getAddress());
        }

    }

    public void sendMessage(Message message) {
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

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getHeadAddress() {
        return headAddress;
    }

    public void setHeadAddress(Address headAddress) {
        this.headAddress = headAddress;
    }

    @SuppressWarnings("all")
    public void createWorker(Addressee target) {
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
                    Thread.sleep(MessageSystem.DEFAULT_STEP_TIME);
                } catch (InterruptedException e) {
                    workerLogger.error("", e);
                    break;
                }
            }
        }).start();
    }

    //TODO make blocking init
    @SuppressWarnings("unchecked")
    private void initWithRemoteHead(String host, int port) {
        try {
            socket = new Socket(host, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Message message = MessageHelper.deserializeMessage(reader);
            message.exec(this);

            startIncomingHandler(reader);
            startOutcomingHandler(writer);

        } catch (Exception e) {
            logger.error("Error during init.", e);
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

    public synchronized void setAddressAddresseeClassMap(ConcurrentHashMap<Address, String> addressAddresseeClassMap) {
        this.addressAddresseeClassMap.clear();
        this.addressAddresseeClassMap.putAll(addressAddresseeClassMap);
    }

    public synchronized ConcurrentHashMap<Address, String> getAddressAddresseeClassMap() {
        return addressAddresseeClassMap;
    }
}
