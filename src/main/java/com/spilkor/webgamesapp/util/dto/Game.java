package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Game {

    final static ObjectMapper mapper = new ObjectMapper();

    protected Group group;

    Game(Group group){
        this.group = group;
    }

    public abstract String getLobbyJSON(UserDTO user);
    public abstract String getGameJSON(UserDTO user);
    public abstract String getEndJSON(UserDTO user);
    public abstract boolean isStartable();
    public abstract boolean updateLobby(String lobbyDataJSON);
    public abstract void startGame();
    public abstract boolean legal(UserDTO userDTO, String moveJSON);
    public abstract void move(UserDTO userDTO, String moveJSON);
}
