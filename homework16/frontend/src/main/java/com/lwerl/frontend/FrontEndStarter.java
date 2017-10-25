package com.lwerl.frontend;

import com.lwerl.frontend.websocket.MessageWebSocket;
import com.lwerl.frontend.websocket.MessageWebSocketHandler;
import com.lwerl.messagesystem.MessageSystemNode;
import com.lwerl.web.FrontendService;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lWeRl on 29.09.2017.
 */
public class FrontEndStarter {

    private static final int PORT = 8030;
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndStarter.class);
    private Server server;

    public void start() {
        this.server = new Server(PORT);

        MessageSystemNode messageSystem = new MessageSystemNode("127.0.0.1", 6666);

        Map<Integer, MessageWebSocket> users = new HashMap<>();
        FrontendService frontendService = new FrontendServiceImpl(users, messageSystem);
        MessageWebSocketHandler handler =  new MessageWebSocketHandler(users, frontendService);

        messageSystem.addAddresseeAs(frontendService, FrontendService.class);

        try {
            server.setHandler(handler);
            server.start();
            LOGGER.info("FrontEndStarter WebSocket server STARTED");
        } catch (Exception e) {
            LOGGER.error("Can't start WebSocket server", e);
        }
    }

    public static void main(String[] args) {
        new FrontEndStarter().start();
    }

}
