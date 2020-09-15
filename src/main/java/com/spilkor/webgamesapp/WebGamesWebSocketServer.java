package com.spilkor.webgamesapp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.util.ConnectionHandler;
import com.spilkor.webgamesapp.util.dto.UserTokenDTO;
import com.spilkor.webgamesapp.util.dto.WSMessage;
import com.spilkor.webgamesapp.util.enums.MessageType;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebGamesWebSocketServer extends WebSocketServer {

    private static ObjectMapper mapper = new ObjectMapper();

    public WebGamesWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            WSMessage wsMessage = mapper.readValue(message, WSMessage.class);
            Long userId = ConnectionHandler.getUserIdByWebSocket(webSocket);
            if (userId == null){
                if (wsMessage.getMessageType() == MessageType.USER_TOKEN){
                    boolean success = authenticate(mapper.readValue(wsMessage.getData(), UserTokenDTO.class), webSocket);
                } else {
                    webSocket.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Could not process WSMessage: " + message);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        ConnectionHandler.removeAndCloseWebSocket(webSocket);
    }

    @Override
    public void onError(WebSocket webSocket, Exception ex) {
        ConnectionHandler.removeAndCloseWebSocket(webSocket);
    }

    private boolean authenticate(UserTokenDTO userTokenDTO, WebSocket webSocket) {
        UserTokenDTO found = ConnectionHandler.getUserTokens().stream().filter(ut -> ut.equals(userTokenDTO)).findFirst().orElse(null);
        if (found != null){
            ConnectionHandler.removeUserToken(found);
            ConnectionHandler.addWebSocket(found.getUserId(), webSocket);
            return true;
        }
        return false;
    }

    public static void sendMessage(WebSocket webSocket, WSMessage message) {
        try {
            String messageJson = mapper.writeValueAsString(message);
            webSocket.send(messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Long userId, WSMessage message) {
        for(WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(userId)){
            sendMessage(webSocket, message);
        }
    }

    public static void broadcastMessage(WSMessage message){
        for (WebSocket webSocket: ConnectionHandler.getWebSockets()) {
            sendMessage(webSocket, message);
        }
    }

}
