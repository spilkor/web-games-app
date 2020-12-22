package com.spilkor.webgamesapp.util.dto;


import com.spilkor.webgamesapp.BusinessManager;
import com.spilkor.webgamesapp.util.ConnectionHandler;
import com.spilkor.webgamesapp.util.GroupHandler;
import com.spilkor.webgamesapp.util.InviteHandler;
import com.spilkor.webgamesapp.util.ServiceHelper;
import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.GameType;
import com.spilkor.webgamesapp.util.enums.UserState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {

    private UserDTO owner;
    private Set<UserDTO> players = new HashSet<>();
    private GameType gameType;
    private GameState gameState;
    private Game game;
    private List<Invite> invites = new ArrayList();

    public Group(UserDTO owner, GameType gameType){
        this.owner = owner;
        players.add(owner);
        this.gameType = gameType;
        gameState = GameState.IN_LOBBY;

        switch (gameType){
            case AMOBA:
                game = Amoba.newInstance(owner);
//            case CHESS:
//                game = Chess.newInstance(owner);
        }
    }

    public GameDataDTO getGameDataDTO(UserDTO user) {
        GameDataDTO gameDataDTO = new GameDataDTO();

        gameDataDTO.setStatable(game == null ? null : GameState.IN_LOBBY.equals(gameState) && game.getStartable(user));
        owner.setUserState(ConnectionHandler.getConnectionsByUserId(owner.getId()).isEmpty() ? UserState.offline : UserState.online);
        gameDataDTO.setOwner(owner);
        players.forEach(player-> player.setUserState(ConnectionHandler.getConnectionsByUserId(player.getId()).isEmpty() ? UserState.offline : UserState.online));
        gameDataDTO.setPlayers(players);
        gameDataDTO.setGameType(gameType);
        gameDataDTO.setGameState(gameState);

        gameDataDTO.setLobbyJSON(game == null ? null : game.getLobbyJSON(user));
        gameDataDTO.setGameJSON(game == null ? null : game.getGameJSON(user));

        return gameDataDTO;
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

    public void joinGroup(Group groupOfUser) {
        players.addAll(groupOfUser.players);
        GroupHandler.destroyGroup(groupOfUser);
    }

    public void invite(Long ownerId, Long friendId) throws InviteException{
        if (!ownerId.equals(owner.getId())){
            throw new InviteException();
        }

        if (invites.stream().anyMatch(invite -> invite.getOwner().getId().equals(ownerId) && invite.getFriend().getId().equals(friendId))){
            throw new InviteException();
        }

        Invite invite = new Invite();
        invite.setOwner(new UserDTO(ServiceHelper.getService(BusinessManager.class).findEagerUserById(ownerId)));
        invite.setFriend(new UserDTO(ServiceHelper.getService(BusinessManager.class).findEagerUserById(friendId)));

        invites.add(invite);

        InviteHandler.addInvite(this, invite);
    }

    public static class InviteException extends Exception{

    }
}
