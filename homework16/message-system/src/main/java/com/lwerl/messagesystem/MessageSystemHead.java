package com.lwerl.messagesystem;

import com.lwerl.messagesystem.model.Address;
import com.lwerl.messagesystem.model.Addressee;
import com.lwerl.messagesystem.util.MessageHelper;
import com.lwerl.messagesystem.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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

    private static final Long ACCEPT_WAITING_TIME = 1000L;

    private final AtomicLong counter = new AtomicLong(0);
    private final LinkedBlockingQueue<Message> headSystemMessageQueue = new LinkedBlockingQueue<>();
    private final Map<Address, LinkedBlockingQueue<String>> receiversQueueMap = new ConcurrentHashMap<>();
    private final Map<Address, Socket> socketNodeMap = new ConcurrentHashMap<>();
    private final List<Address> nodes = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, List<Address>> addresseeClassAddressesMap = new ConcurrentHashMap<>();
    private final Address address = new Address(counter.incrementAndGet());
    private final int port;

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

                Address newAddress = new Address(counter.incrementAndGet());
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
                    String messageJson = MessageHelper.getMessageJson(reader);
                    Address destination = MessageHelper.getDestination(messageJson);
                    if (getAddress().equals(destination)) {
                        Message message = MessageHelper.deserializeMessage(messageJson);
                        headSystemMessageQueue.offer(message);
                    } else {
                        receiversQueueMap.get(destination).offer(messageJson);
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

    void finishNodeRegistration(Address nodeAddress) {
        try {

            Socket socket = socketNodeMap.get(nodeAddress);
            nodes.add(nodeAddress);
            final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
            receiversQueueMap.put(nodeAddress, queue);
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(() -> {
                while (true) {
                    try {
                        String message = queue.take();
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
            String updateAddressMap = MessageHelper.serializeMessage(
                    new AddresseeMapUpdateMessage(getAddress(), nodeAddress, addresseeClassAddressesMap));

            queue.offer(updateAddressMap);

        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    void addRemoteAddressee(AddAddresseeRequestMessage requestMessage) {
        Address from = requestMessage.getTo();
        Address to = requestMessage.getFrom();
        Long waitingId = requestMessage.getWaitingId();
        Address establishedAddress = new Address(counter.incrementAndGet());

        LinkedBlockingQueue<String> queue = receiversQueueMap.get(to);
        receiversQueueMap.put(establishedAddress, queue);

        String addAddresseeMessage = MessageHelper.serializeMessage(
                new AddAddresseeMessage(from, to, waitingId, establishedAddress));

        queue.offer(addAddresseeMessage);

        List<Address> list = addresseeClassAddressesMap.get( requestMessage.getFullClassName());
        if(list == null ) {
            List<Address> newList = new CopyOnWriteArrayList<>();
            newList.add(establishedAddress);
            addresseeClassAddressesMap.put(requestMessage.getFullClassName(), newList);
        } else {
            list.add(establishedAddress);
        }

        nodes.forEach(nodeAddress -> {
            String updateAddressMap = MessageHelper.serializeMessage(
                    new AddresseeMapUpdateMessage(getAddress(), nodeAddress, addresseeClassAddressesMap));

            receiversQueueMap.get(nodeAddress).offer(updateAddressMap);
        });
    }
}
