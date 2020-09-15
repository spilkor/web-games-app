package com.spilkor.webgamesapp.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spilkor.webgamesapp.util.enums.MessageType;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WSMessage implements Serializable{

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
