package com.lwerl.messagenode;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;
import com.lwerl.messagenode.model.message.*;
import com.lwerl.messagenode.model.message.system.AddAddresseeMessage;
import com.lwerl.messagenode.model.message.system.AddAddresseeRequestMessage;
import com.lwerl.messagenode.model.message.system.AddresseeMapUpdateMessage;
import com.lwerl.messagenode.model.message.system.NodeInitMessage;
import com.lwerl.messagenode.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class MessageSystemHead implements Addressee {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSystemHead.class);
    private static final AtomicLong COUNTER = new AtomicLong(0);
    private static final Long ACCEPT_WAITING_TIME = 1000L;

    private final LinkedBlockingQueue<Message> headSystemMessageQueue = new LinkedBlockingQueue<>();
    private final Map<Address, LinkedBlockingQueue<Message>> receiversQueueMap = new ConcurrentHashMap<>();
    private final Map<Address, Socket> socketNodeMap = new ConcurrentHashMap<>();
    private final List<Address> nodes = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<Address,String> addressAddresseeClassMap = new ConcurrentHashMap<>();

    private final Address address = new Address(COUNTER.incrementAndGet());
    private int port;

    public MessageSystemHead(int port) {
        this.port = port;
    }


    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        // Not need
    }

    public void start() {
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        initSocket(socket);
                    } catch (IOException e) {
                        LOGGER.error("", e);
                        try {
                            serverSocket.close();
                        } catch (IOException e1) {
                        }
                        break;
                    }
                }
            }).start();
            startSystemWorker();
        } catch (IOException e) {
            LOGGER.error("", e);
        }

    }

    private void initSocket(final Socket socket) {
        new Thread(() -> {
            try {

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Address newAddress = new Address(COUNTER.incrementAndGet());
                socketNodeMap.put(newAddress, socket);

                String initMessage =
                        MessageHelper.serializeMessage(new NodeInitMessage(getAddress(), newAddress));

                writer.write(initMessage);
                writer.flush();

                startIncomingHandler(reader);

                Thread.sleep(ACCEPT_WAITING_TIME);
                if (receiversQueueMap.get(newAddress) == null) {
                    socketNodeMap.remove(newAddress);
                    socket.close();
                }
            } catch (IOException | InterruptedException e) {
                LOGGER.error("", e);
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    private void startSystemWorker() {
        new Thread(() -> {
            while (true) {
                try {
                    headSystemMessageQueue.take().exec(this);
                } catch (InterruptedException e) {
                    LOGGER.error("", e);
                    break;
                }
            }
        }).start();
    }

    private void startIncomingHandler(final BufferedReader reader) {
        new Thread(() -> {
            while (true) {
                try {
                    Message message = MessageHelper.deserializeMessage(reader);
                    if (getAddress().equals(message.getTo())) {
                        headSystemMessageQueue.offer(message);
                    } else {
                        receiversQueueMap.get(message.getTo()).offer(message);
                    }
                } catch (SocketException e) {
                    break;
                } catch (Exception e) {
                    LOGGER.error("", e);
                    try {
                        reader.ready();
                    } catch (IOException e1) {
                        break;
                    }
                }
            }
        }).start();
    }

    public void finishNodeRegistration(Address nodeAddress) {
        try {

            Socket socket = socketNodeMap.get(nodeAddress);
            nodes.add(nodeAddress);
            final LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
            receiversQueueMap.put(nodeAddress, queue);
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(() -> {
                while (true) {
                    try {
                        String message = MessageHelper.serializeMessage(queue.take());
                        writer.write(message);
                        writer.flush();
                    } catch (IOException e) {
                        LOGGER.error("", e);
                        break;
                    } catch (Exception e) {
                        LOGGER.error("", e);
                    }
                }
            }).start();

            // Отсылаем карту зарегистрованных адресов

            queue.offer(new AddresseeMapUpdateMessage(getAddress(), nodeAddress, addressAddresseeClassMap));

        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    public void addRemoteAddressee(AddAddresseeRequestMessage requestMessage) {
        Address from = requestMessage.getTo();
        Address to = requestMessage.getFrom();
        Long waitingId = requestMessage.getWaitingId();
        Address establishedAddress = new Address(COUNTER.incrementAndGet());

        LinkedBlockingQueue<Message> queue = receiversQueueMap.get(to);
        receiversQueueMap.put(establishedAddress, queue);

        queue.offer(new AddAddresseeMessage(from, to, waitingId, establishedAddress));

        addressAddresseeClassMap.put(establishedAddress, requestMessage.getFullClassName());

        nodes.forEach(nodeAddress -> receiversQueueMap.get(nodeAddress)
                .offer(new AddresseeMapUpdateMessage(getAddress(), nodeAddress, addressAddresseeClassMap)));
    }
}
