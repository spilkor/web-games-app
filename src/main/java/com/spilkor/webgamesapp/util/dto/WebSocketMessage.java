package com.spilkor.webgamesapp.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessage implements Serializable{

    private WebSocketMessageType messageType;
    private String data;

    public WebSocketMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(WebSocketMessageType messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
