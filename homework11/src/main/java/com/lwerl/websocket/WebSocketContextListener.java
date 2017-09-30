package com.lwerl.websocket;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by lWeRl on 29.09.2017.
 */
public class WebSocketContextListener implements ServletContextListener {

    private static final int PORT = 8030;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketContextListener.class);

    @Autowired
    private MessageWebSocketHandler handler;

    private Server server;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.server = new Server(PORT);

        try {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            server.setHandler(handler);
            server.start();
            LOGGER.info("Start WebSocket server");
        } catch (Exception e) {
            LOGGER.error("Can't start WebSocket server", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (server != null) {
            try {
                server.stop();
                LOGGER.info("Stop WebSocket server");
            } catch (Exception e) {
                LOGGER.error("Can't stop WebSocket server", e);
            }
        }
    }
}
