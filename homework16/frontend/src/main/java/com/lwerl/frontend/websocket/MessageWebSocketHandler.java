package com.lwerl.frontend.websocket;

import com.lwerl.web.FrontendService;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.util.Map;

/**
 * Created by lWeRl on 29.09.2017.
 */

public class MessageWebSocketHandler extends WebSocketHandler {

    private static final int LOGOUT_TIME = 10 * 60 * 1000;

    private MessageWebSocketCreator creator;

    public MessageWebSocketHandler(Map<Integer, MessageWebSocket> users, FrontendService service) {
        this.creator = new MessageWebSocketCreator(users, service);
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(creator);
    }
}
