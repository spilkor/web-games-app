package com.spilkor.webgamesapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.BusinessManager;
import com.spilkor.webgamesapp.WebSocketMessageSender;
import com.spilkor.webgamesapp.model.User;
import com.spilkor.webgamesapp.util.dto.*;
import com.spilkor.webgamesapp.util.enums.UserState;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class InviteHandler {

    private static ObjectMapper mapper = new ObjectMapper();

    private static List<Invite> invites = new ArrayList<>();

    private static Map<Group, List<Invite>> groupMap = new HashMap<>();

    public static List<Invite> getInvites(UserDTO user) {
        return invites.stream().filter(invite -> invite.getOwner().getId().equals(user.getId()) || invite.getFriend().getId().equals(user.getId())).collect(Collectors.toList());
    }


    public static void addInvite(Group group, Invite invite) {
        if (groupMap.containsKey(group)) {
            List<Invite> invites = new ArrayList<>();
            invites.add(invite);
            invites.addAll(groupMap.get(group));
            groupMap.put(group, invites);
        } else {
            groupMap.put(group, Arrays.asList(invite));
        }
        invites.add(invite);

        updateInviteList(invite.getOwner());
        updateInviteList(invite.getFriend());
    }

    private static void updateInviteList(UserDTO userDTO) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessageType.INVITE_LIST);
        try {
            webSocketMessage.setData(mapper.writeValueAsString(getInvites(userDTO)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(userDTO.getId(), webSocketMessage);
    }
}
