package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.util.ConnectionHandler;
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
    private Set<UserDTO> invitedUsers = new HashSet<>();

    protected Game(UserDTO owner, GameType gameType){
        this.owner = owner;
        this.gameType = gameType;
        players.add(owner);
        gameState = GameState.IN_LOBBY;
    }

    public GameDTO getGameDTO(UserDTO user) {
        GameDTO gameDTO = new GameDTO();

        gameDTO.setStartable(owner.equals(user) && GameState.IN_LOBBY.equals(gameState) && isStartable());
        owner.setUserState(ConnectionHandler.getUserState(owner.getId()));
        gameDTO.setOwner(owner);
        players.forEach(player-> player.setUserState(ConnectionHandler.getUserState(player.getId())));
        gameDTO.setPlayers(players);
        gameDTO.setInvitedUsers(invitedUsers);
        gameDTO.setGameType(gameType);
        gameDTO.setGameState(gameState);

        gameDTO.setGameJSON(getGameJSON(user));

        return gameDTO;
    }

    public abstract String getGameJSON(UserDTO player);
    public abstract boolean isStartable();
    public abstract void start();
    public abstract void restart();
    public abstract boolean updateLobby(String lobbyJSON);
    public abstract boolean legal(UserDTO userDTO, String moveJSON);
    public abstract void move(UserDTO userDTO, String moveJSON);


    public UserDTO getSecondPlayer(){
        return players.stream().filter(player -> !player.equals(owner)).findFirst().orElse(null);
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public Set<UserDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UserDTO> players) {
        this.players = players;
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

    public Set<UserDTO> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(Set<UserDTO> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
