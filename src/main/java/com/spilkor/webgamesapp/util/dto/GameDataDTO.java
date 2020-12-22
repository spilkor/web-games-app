package com.spilkor.webgamesapp.util.dto;

import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.GameType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GameDataDTO implements Serializable {

    private Boolean statable;
    private GameType gameType;
    private GameState gameState;
    private Set<UserDTO> players = new HashSet<>();
    private UserDTO owner;

    private String lobbyJSON;
    private String gameJSON;


    public boolean getStatable() {
        return statable;
    }

    public void setStatable(Boolean statable) {
        this.statable = statable;
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

    public String getLobbyJSON() {
        return lobbyJSON;
    }

    public void setLobbyJSON(String lobbyJSON) {
        this.lobbyJSON = lobbyJSON;
    }

    public String getGameJSON() {
        return gameJSON;
    }

    public void setGameJSON(String gameJSON) {
        this.gameJSON = gameJSON;
    }
}
