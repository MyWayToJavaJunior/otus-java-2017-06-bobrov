package com.lwerl.configuration;

import com.lwerl.websocket.MessageWebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lWeRl on 30.09.2017.
 */
@Configuration
public class SpringConfiguration {

    @Bean
    public Map<Integer, MessageWebSocket> messageWebSockets() {
        return new ConcurrentHashMap<>();
    }
}
