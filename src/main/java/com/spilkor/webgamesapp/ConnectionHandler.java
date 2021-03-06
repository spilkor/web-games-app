package com.spilkor.webgamesapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.model.dto.UserTokenDTO;
import com.spilkor.webgamesapp.model.dto.WebSocketMessage;
import com.spilkor.webgamesapp.model.User;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.ServiceHelper;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public abstract class ConnectionHandler {

    private static Map<Long, List<Session>> userConnections = new HashMap<>();
    private static List<UserTokenDTO> userTokens = new ArrayList<>();
    private static UserTokenRemover userTokenRemover = null;

    public static List<UserTokenDTO> getUserTokens() {
        return userTokens;
    }

    public static Long getUserIdByConnection(Session session) {
        for (Map.Entry<Long, List<Session>> entry: userConnections.entrySet()) {
            if (entry.getValue().contains(session)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void addConnection(Long userId, Session session) {
        if (userConnections.containsKey(userId)) {
            List<Session> connections = new ArrayList<>();
            connections.add(session);
            connections.addAll(userConnections.get(userId));
            userConnections.put(userId, connections);
        } else {
            userConnections.put(userId, Arrays.asList(session));

            List<User> toUpdate = new ArrayList<>();
            User user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(userId);
            toUpdate.addAll(user.getFriends());
            updateFriendList(toUpdate);
        }
    }

    public static void updateFriendList(List<User> userList) {
        userList.forEach(ConnectionHandler::updateFriendList);
    }

    private static void updateFriendList(User user) {
        user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(user.getId());

        List<UserDTO> friends = new ArrayList<>();
        for (User friend: user.getFriends()){
            UserDTO userDTO = new UserDTO(friend);
            userDTO.setUserState(ConnectionHandler.getUserState(friend.getId()));
            friends.add(userDTO);
        }

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessage.MessageType.FRIEND_LIST);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(friends));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(user.getId(), webSocketMessage);
    }

    public static void removeAndCloseConnections(Long userId) {
        List<Session> connectionsOfUser = userConnections.get(userId);
        if (connectionsOfUser != null){
            connectionsOfUser.forEach(ConnectionHandler::removeAndCloseConnection);
        }
    }

    private static void removeAndCloseConnection(Session session) {
        removeConnection(session);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeConnection(Session session) {
        Long userId = getUserIdByConnection(session);
        if (userId != null) {
            removeConnection(userId, session);
        }
    }

    private static void removeConnection(Long userId, Session session) {
        List<Session> connections = new ArrayList<>(userConnections.get(userId));
        connections.remove(session);

        if (connections.isEmpty()){
            userConnections.remove(userId);

            User user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(userId);
            updateFriendList(new ArrayList<>(user.getFriends()));
        } else {
            userConnections.put(userId, connections);
        }
    }

    public static List<Session> getConnections() {
        List<Session> result = new ArrayList<>();
        for(Map.Entry<Long, List<Session>> entry: userConnections.entrySet()){
            result.addAll(entry.getValue());
        }
        return result;
    }

    public static List<Session> getConnectionsByUserId(Long userId) {
        return userConnections.containsKey(userId) ? userConnections.get(userId) : new ArrayList<>();
    }

    public static UserDTO.UserState getUserState(Long userId){
        return ConnectionHandler.getConnectionsByUserId(userId).isEmpty() ? UserDTO.UserState.offline : UserDTO.UserState.online;
    }

    public static Set<Long> getAllUserIds() {
        return userConnections.keySet();
    }

    public static void addUserToken(UserTokenDTO userToken) {
        userTokens.add(userToken);
    }

    public static void startUserTokenRemover(){
        if (userTokenRemover == null){
            userTokenRemover = new UserTokenRemover();
            userTokenRemover.run();
        }
    }

    public static void updateFriendRequestList(User user) {

        user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(user.getId());

        List<UserDTO> friendRequestList = new ArrayList<>();
        for (User request: user.getRequestedBy()){
            UserDTO userDTO = new UserDTO(request);
            friendRequestList.add(userDTO);
        }

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessage.MessageType.FRIEND_REQUEST_LIST);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(friendRequestList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(user.getId(), webSocketMessage);

    }

    private static class UserTokenRemover extends Thread {

        private boolean running = false;
        private List<UserTokenDTO> toRemove = new ArrayList<>();

        @Override
        public void run() {
            running = true;
            try {
                while (running){
                    Thread.sleep(10000);
                    userTokens.remove(toRemove);
                    toRemove.clear();
                    toRemove.addAll(ConnectionHandler.getUserTokens());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void _stop() {
            running = false;
        }
    }

}
