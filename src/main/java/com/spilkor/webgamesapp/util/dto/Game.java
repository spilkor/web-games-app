package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Game {

    protected final static ObjectMapper mapper = new ObjectMapper();

    protected Group group;

    public Game(Group group){
        this.group = group;
    }

    public abstract String getGameJSON(UserDTO user);

    public abstract String getLobbyJSON(UserDTO user);

    public abstract boolean isStartable();

    public abstract boolean updateLobby(String lobbyDataJSON);

    public abstract void startGame();


}
