package com.spilkor.webgamesapp;


import com.spilkor.webgamesapp.model.dto.UserTokenDTO;
import com.spilkor.webgamesapp.model.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.Mapper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/websocket")
public class WebGamesWebSocketEndPoint {

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
            WebSocketMessage webSocketMessage = Mapper.readValue(message, WebSocketMessage.class);
            Long userId = ConnectionHandler.getUserIdByConnection(session);
            if (userId == null) {
                if (webSocketMessage.getMessageType() == WebSocketMessage.MessageType.USER_TOKEN) {
                    UserTokenDTO userTokenDTO = Mapper.readValue(webSocketMessage.getData(), UserTokenDTO.class);
                    if(ConnectionHandler.getUserTokens().remove(userTokenDTO)){
                        ConnectionHandler.addConnection(userTokenDTO.getUserId(), session);
                    }
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
