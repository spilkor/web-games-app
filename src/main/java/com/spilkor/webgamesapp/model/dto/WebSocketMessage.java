package com.spilkor.webgamesapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessage implements Serializable{

    public enum MessageType {
        STATE,
        USER_TOKEN,
        CHAT_MESSAGE,
        LOBBY,
        INVITE,
        FRIEND_LIST,
        GAME,
        INVITE_LIST
    }

    private MessageType messageType;
    private String data;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
