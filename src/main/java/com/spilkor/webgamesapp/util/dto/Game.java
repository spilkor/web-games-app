package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.GameType;

import java.util.HashSet;
import java.util.Set;

public abstract class Game {

    protected final static ObjectMapper mapper = new ObjectMapper();

    protected UserDTO owner;
    protected Set<UserDTO> players = new HashSet<>();
    protected GameType gameType;
    protected GameState gameState;
    private Game game;

    public Game(UserDTO owner){
        this.owner = owner;
        players.add(owner);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public void setPlayers(Set<UserDTO> players) {
        this.players = players;
    }

    public void addPlayer(UserDTO player){
        players.add(player);
    }

    public void joinUser(UserDTO user) {
        players.add(user);
    }

    public Set<UserDTO> getPlayers() {
        return players;
    }

    public abstract String getGameJSON(UserDTO user);

    public abstract String getLobbyJSON(UserDTO user);

    public abstract boolean getStartable(UserDTO user);

    public abstract boolean updateLobby(Group group, String lobbyDataJSON);
}
