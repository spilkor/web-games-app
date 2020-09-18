package com.spilkor.webgamesapp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.util.ConnectionHandler;
import com.spilkor.webgamesapp.util.dto.WebSocketMessage;

import javax.websocket.Session;
import java.io.IOException;

public final class WebSocketMessageSender  {

    private static ObjectMapper mapper = new ObjectMapper();

    private WebSocketMessageSender(){};

    public static void sendMessage(Session connection, WebSocketMessage webSocketMessage) {
        try {
            String messageJson = mapper.writeValueAsString(webSocketMessage);
            connection.getBasicRemote().sendText(messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Long userId, WebSocketMessage message) {
        for(Session connection : ConnectionHandler.getConnectionsByUserId(userId)){
            sendMessage(connection, message);
        }
    }

    public static void broadcastMessage(WebSocketMessage message){
        for (Session connection: ConnectionHandler.getConnections()) {
            sendMessage(connection, message);
        }
    }

}
