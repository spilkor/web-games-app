package com.spilkor.webgamesapp.game;


import com.spilkor.webgamesapp.model.dto.GameDTO;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.ConnectionHandler;

import java.util.HashSet;
import java.util.Set;

public abstract class Game {

    public enum GameState {
        IN_LOBBY,
        IN_GAME,
        ENDED
    }

    public enum GameType {
        AMOBA,
        CHESS,
        CARCASSONNE,
        SNAPSZER
    }

    private boolean locked = true;
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

    public abstract String getGameJSON(UserDTO player);
    public abstract boolean isStartable();
    public abstract void start();
    public abstract void restart();
    public abstract boolean updateLobby(String lobbyJSON);
    public abstract boolean legal(UserDTO userDTO, String moveJSON);
    public abstract void move(UserDTO userDTO, String moveJSON);
    public abstract void surrender(UserDTO userDTO);

    public void playerJoined(UserDTO player){
    }

    public void playerLeft(UserDTO player){
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

    public UserDTO getSecondPlayer(){
        return players.stream().filter(player -> !player.equals(owner)).findFirst().orElse(null);
    }

    public boolean lock(){
        if (locked){
            return false;
        } else {
            locked = true;
            return true;
        }
    }

    public boolean unLock(){
        if (locked){
            locked = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean isLocked() {
        return locked;
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
