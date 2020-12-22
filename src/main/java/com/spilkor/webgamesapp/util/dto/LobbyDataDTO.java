package com.spilkor.webgamesapp.util.dto;

import java.util.ArrayList;
import java.util.List;

public class LobbyDataDTO {

    private String gameType;
    private UserDTO user;
    private List<UserDTO> users = new ArrayList<>();

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
