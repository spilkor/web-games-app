package com.spilkor.webgamesapp.util.dto;

public class ChatMessageDTO {

    private String message;
    private UserDTO user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
