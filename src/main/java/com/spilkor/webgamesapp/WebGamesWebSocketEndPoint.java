package com.spilkor.webgamesapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.util.ConnectionHandler;
import com.spilkor.webgamesapp.util.dto.UserTokenDTO;
import com.spilkor.webgamesapp.util.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/websocket")
public class WebGamesWebSocketEndPoint {

    private ObjectMapper mapper = new ObjectMapper();
    private Session session = null;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        if (ConnectionHandler.getConnections().contains(session)){
            return;
        }

        try {
            WebSocketMessage webSocketMessage = mapper.readValue(message, WebSocketMessage.class);
            Long userId = ConnectionHandler.getUserIdByConnection(session);
            if (userId == null) {
                if (webSocketMessage.getMessageType() == WebSocketMessageType.USER_TOKEN) {
                    ConnectionHandler.authenticate(mapper.readValue(webSocketMessage.getData(), UserTokenDTO.class), session);
                }
            }
        } catch (Exception e) {
            System.out.println("Could not process WebSocketMessage: " + message);
        }
    }

    @OnClose
    public void onClose() {
        ConnectionHandler.removeConnection(session);
    }

    @OnError
    public void onError(Throwable t) {
        ConnectionHandler.removeConnection(session);
    }

}
