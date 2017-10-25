package com.lwerl.frontend.websocket;



import com.lwerl.web.FrontendService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

public class MessageWebSocketCreator implements WebSocketCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWebSocketCreator.class);

    private Map<Integer, MessageWebSocket> users;
    private FrontendService frontendService;

    public MessageWebSocketCreator(Map<Integer, MessageWebSocket> users, FrontendService frontendService) {
        this.users = users;
        this.frontendService = frontendService;
        LOGGER.info("WebSocketCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        MessageWebSocket socket = new MessageWebSocket(users, frontendService);
        LOGGER.info("Socket created");
        return socket;
    }

}
