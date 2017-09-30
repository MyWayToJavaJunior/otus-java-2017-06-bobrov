package com.lwerl.websocket;

import com.lwerl.frontend.FrontendService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lWeRl on 29.09.2017.
 */

@WebSocket
public class MessageWebSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWebSocket.class);
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private Map<Integer, MessageWebSocket> users;
    private FrontendService frontendService;
    private Session session;
    private Integer id;

    public MessageWebSocket(Map<Integer, MessageWebSocket> users, FrontendService frontendService) {
        this.users = users;
        this.frontendService = frontendService;
        this.id = COUNTER.incrementAndGet();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOGGER.info("Connect: " + session.getRemoteAddress().getAddress());
        users.put(id, this);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        try {
            Long userId = Long.parseLong(message);
            frontendService.proceedRequestUserById(userId, id);
        } catch (Exception e) {
            LOGGER.error("Parse user id error", e);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOGGER.info("Close: statusCode=" + statusCode + ", reason=" + reason);
        users.remove(this);
        session.close();
    }

    public void sendMessage(String message) {
        try {
            session.getRemote().sendString(message);
        } catch (Exception e) {
            LOGGER.error("Send to web socket error", e);
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageWebSocket that = (MessageWebSocket) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
