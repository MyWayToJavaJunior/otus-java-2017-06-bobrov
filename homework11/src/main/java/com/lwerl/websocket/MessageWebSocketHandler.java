package com.lwerl.websocket;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lWeRl on 29.09.2017.
 */
@Component
public class MessageWebSocketHandler extends WebSocketHandler {

    private static final int LOGOUT_TIME = 10 * 60 * 1000;

    @Autowired
    private MessageWebSocketCreator creator;

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(creator);
    }
}
