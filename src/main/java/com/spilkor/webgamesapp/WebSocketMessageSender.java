package com.spilkor.webgamesapp;


import com.spilkor.webgamesapp.model.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.Mapper;

import javax.websocket.Session;
import java.io.IOException;

public final class WebSocketMessageSender  {

    private WebSocketMessageSender(){
    }

    public static void sendMessage(Session connection, WebSocketMessage webSocketMessage) {
        try {
            String messageJson = Mapper.writeValueAsString(webSocketMessage);
            connection.getBasicRemote().sendText(messageJson);
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
