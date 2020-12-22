package com.spilkor.webgamesapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.spilkor.webgamesapp.WebSocketMessageSender;
import com.spilkor.webgamesapp.util.dto.GameDataDTO;
import com.spilkor.webgamesapp.util.dto.Group;
import com.spilkor.webgamesapp.util.dto.UserDTO;
import com.spilkor.webgamesapp.util.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.enums.GameType;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;
import org.java_websocket.WebSocket;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupHandler {
    private static ObjectMapper mapper = new ObjectMapper();

    private static List<Group> groups = new ArrayList<>();

    public static Group createGroup(UserDTO owner, GameType gameType){
        Group group = new Group(owner, gameType);
        groups.add(group);

        updateUser(owner);

        return group;
    }

    public static Group getGroupOfUser(UserDTO user) {
        for (Group group: groups){
            if (group.getPlayers().contains(user)){
                return group;
            }
        }
        return null;
    }

    public static void destroyGroup(Group group) {
        groups.remove(group);
        InviteHandler.removeGroup(group);
    }

    public static void updateGroup(Group group) {
        group.getPlayers().forEach(user-> updateUser(user));
    }

    public static void leaveGroup(UserDTO user) {
        Group group = getGroupOfUser(user);
        Set<UserDTO> users = group.getPlayers();

        //Egyszemélyes group-okat megszüntetjük
        if (group.getPlayers().size() == 2){
            destroyGroup(group);
            users.forEach(u-> updateUser(u));
            return;
        }

        //Kivesszük a users-ből
        group.setPlayers(group.getPlayers().stream().filter(u-> !u.equals(user)).collect(Collectors.toSet()));

        //Ha ő volt az owner, a group-ja owner-t vált
        if (group.getOwner().equals(user)){
            group.setOwner(new ArrayList<>(group.getPlayers()).get(0));
        }

        users.forEach(u-> updateUser(u));
    }

    private static void updateUser(UserDTO userDTO) {
        GameDataDTO gameDataDTO = null;

        Group group = getGroupOfUser(userDTO);
        if (group != null){
            gameDataDTO = group.getGameDataDTO(userDTO);
        }

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessageType.GAME_DATA);
        try {
            webSocketMessage.setData(mapper.writeValueAsString(gameDataDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(userDTO.getId(), webSocketMessage);
    }

}
