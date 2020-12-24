package com.spilkor.webgamesapp.util.dto;

import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.GameType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GameDTO implements Serializable {

    private Boolean startable;
    private GameType gameType;
    private GameState gameState;
    private Set<UserDTO> players = new HashSet<>();
    private UserDTO owner;
    private Set<UserDTO> invitedUsers = new HashSet<>();

    private String gameJSON;


    public Boolean getStartable() {
        return startable;
    }

    public void setStartable(Boolean startable) {
        this.startable = startable;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Set<UserDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UserDTO> players) {
        this.players = players;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public String getGameJSON() {
        return gameJSON;
    }

    public void setGameJSON(String gameJSON) {
        this.gameJSON = gameJSON;
    }

    public Set<UserDTO> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(Set<UserDTO> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
