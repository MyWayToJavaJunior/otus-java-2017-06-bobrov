package com.lwerl.messagenode;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;
import com.lwerl.messagenode.model.message.NodeInitMessage;
import com.lwerl.messagenode.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lWeRl on 21.10.2017.
 */
public class MessageSystemHead implements Addressee {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSystemHead.class);
    private static final AtomicLong COUNTER = new AtomicLong(0);
    private static final Long ACCEPT_WAITING_TIME = 1000L;
    private final Map<Address, BufferedWriter> writersMap = new ConcurrentHashMap<>();

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

    private void init() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            new Thread(() -> {
                while(true) {
                    try {
                        Socket socket = serverSocket.accept();
                        initSocket(socket);
                    } catch (IOException e) {
                        LOGGER.error("", e);
                    }
                }
            }).start();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    private void initSocket(final Socket socket) {
        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Address newAddress = new Address(COUNTER.incrementAndGet());
                String initMessage =
                        MessageHelper.serializeMessage(new NodeInitMessage(getAddress(), newAddress));
                writer.write(initMessage);
                writer.flush();
                Thread.sleep(ACCEPT_WAITING_TIME);
                if (writersMap.get(newAddress) == null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
