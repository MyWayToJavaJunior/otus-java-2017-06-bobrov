package com.lwerl.messagenode.connection;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created by lWeRl on 02.10.2017.
 */
public class NodeConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeConnection.class);


    private Socket socket;
    private Addressee addressee;
    private boolean isConnected;
    private Thread nodeThread;

    public NodeConnection(Addressee addressee) {
        this.addressee = addressee;
    }

    public Address getAddress() {
        return addressee.getAddress();
    }

    public void connect(String host, int port) {
        if (socket != null) {
            LOGGER.info("Node already connected.");
        } else {
            try {
                socket = new Socket(host, port);
                handshake();
                if (isConnected) {
                    nodeThread = new Thread(() -> {
                        
                    });
                }
            } catch (IOException e) {
                LOGGER.error("Can't connect to message system!", e);
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    private void handshake() throws IOException {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output.write(addressee.getClass().getName());
        output.flush();
        String response = input.readLine();
        try {
            Long id = Long.parseLong(response);
            addressee.setAddress(new Address(id));
            isConnected = true;
        } catch (NumberFormatException e) {
            socket.close();
            socket = null;
            LOGGER.error("Handshake with class " + addressee.getClass().getName() + " refused!");
        }
    }
}
