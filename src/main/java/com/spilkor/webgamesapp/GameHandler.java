package com.spilkor.webgamesapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.game.amoba.Amoba;
import com.spilkor.webgamesapp.model.dto.GameDTO;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.model.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.ServiceHelper;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class GameHandler {

    private static Set<Game> games = new HashSet<>();

    static Game createGame(UserDTO owner, Game.GameType gameType){
        Game game = null;
        switch (gameType){
            case AMOBA:
                game = new Amoba(owner, gameType);
                break;
            case CHESS:
//                game = new Chess(owner, gameType);
//                break;
        }

        games.add(game);

        return game;
    }

    static Game getGameOfUser(UserDTO user) {
        return games.stream().filter(game -> game.getPlayers().contains(user)).findFirst().orElse(null);
    }

//    public static void destroyGame(Game game) {
//        games.remove(game);
//        //TODO
////        InviteHandler.removeGroup(group);
//    }

//    public static void leaveGroup(UserDTO user) {
//        Group group = getGroupOfUser(user);
//        Set<UserDTO> users = group.getPlayers();
//
//        //Egyszemélyes group-okat megszüntetjük
//        if (group.getPlayers().size() == 2){
//            destroyGroup(group);
//            users.forEach(u-> updateUser(u));
//            return;
//        }
//
//        //Kivesszük a users-ből
//        group.setPlayers(group.getPlayers().stream().filter(u-> !u.equals(user)).collect(Collectors.toSet()));
//
//        //Ha ő volt az owner, a group-ja owner-t vált
//        if (group.getOwner().equals(user)){
//            group.setOwner(new ArrayList<>(group.getPlayers()).get(0));
//        }
//
//        users.forEach(u-> updateUser(u));
//    }

    private static void updateInviteList(UserDTO userDTO) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessage.MessageType.INVITE_LIST);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(getInvites(userDTO)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(userDTO.getId(), webSocketMessage);
    }


    static void updatePlayers(Game game) {
        game.getPlayers().forEach(player-> updatePlayer(player, game));
    }

    static void startGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.getOwner().equals(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!Game.GameState.IN_LOBBY.equals(game.getGameState()) || !game.isStartable()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        Set<UserDTO> invitedUsers = game.getInvitedUsers();
        game.setInvitedUsers(new HashSet<>());
        invitedUsers.forEach(GameHandler::updateInviteList);

        game.setGameState(Game.GameState.IN_GAME);
        game.start();

        updatePlayers(game);
    }


    private static void updatePlayer(UserDTO player, Game game) {
        GameDTO gameDTO = game == null ? null : game.getGameDTO(player);

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessage.MessageType.GAME);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(gameDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(player.getId(), webSocketMessage);
    }

    public static void updatePlayer(UserDTO player) {
        Game game = getGameOfUser(player);
        updatePlayer(player, game);
    }

    static Set<UserDTO> getInvites(UserDTO user) {
        Set<UserDTO> ownersOfInvites = new HashSet<>();
        games.forEach(game -> game.getInvitedUsers().forEach(invitedUser-> {
            if (invitedUser.equals(user)){
                ownersOfInvites.add(game.getOwner());
            }
        }));
        return ownersOfInvites;
    }

    public static void invite(UserDTO user, UserDTO friend) throws WebGamesApi.WebGamesApiException {
        Game game = GameHandler.getGameOfUser(user);

        if (game == null || !Game.GameState.IN_LOBBY.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!ServiceHelper.getService(BusinessManager.class).friends(user.getId(), friend.getId()) || !game.getOwner().getId().equals(user.getId())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!game.getInvitedUsers().add(friend)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FOUND);
        }

        game.getInvitedUsers().add(friend);

        updateInviteList(user);
        updateInviteList(friend);

        updatePlayers(game);
    }

    static void acceptInvite(UserDTO user, UserDTO owner) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(owner);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!Game.GameState.IN_LOBBY.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!game.getOwner().equals(owner)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!game.getInvitedUsers().remove(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        updateInviteList(user);

        game.getPlayers().add(user);

        updatePlayers(game);
    }

    static void declineInvite(UserDTO user, UserDTO owner) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(owner);

        if (game == null || !game.getInvitedUsers().remove(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        updateInviteList(user);

        updatePlayers(game);
    }

    static void restartGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.getOwner().equals(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!Game.GameState.ENDED.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        game.setGameState(Game.GameState.IN_LOBBY);

        game.restart();

        updatePlayers(game);
    }

    public static void move(UserDTO user, String moveJSON) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!Game.GameState.IN_GAME.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!game.legal(user, moveJSON)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_BAD_REQUEST);
        }

        game.move(user, moveJSON);

        GameHandler.updatePlayers(game);
    }

    public static void leaveGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (Game.GameState.IN_GAME.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }


        if(game.getPlayers().size() == 1){
            removeGame(game);
            return;
        }

        if (game.getOwner().equals(user)){
            game.setOwner(game.getSecondPlayer());
        }
        game.getPlayers().remove(user);

        updatePlayers(game);
        updatePlayer(user);
    }

    private static void removeGame(Game game) {
        games.remove(game);

        game.getPlayers().forEach(GameHandler::updatePlayer);
        game.getInvitedUsers().forEach(GameHandler::updateInviteList);
    }

    public static void kickPlayer(UserDTO owner, UserDTO player) throws WebGamesApi.WebGamesApiException  {
        Game game = getGameOfUser(owner);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.getOwner().equals(owner)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (Game.GameState.IN_GAME.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (owner.equals(player)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!game.getPlayers().remove(player)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        updatePlayers(game);
        updatePlayer(player);
    }
}
