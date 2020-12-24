package com.spilkor.webgamesapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.BusinessManager;
import com.spilkor.webgamesapp.WebGamesApi;
import com.spilkor.webgamesapp.WebSocketMessageSender;
import com.spilkor.webgamesapp.util.dto.*;
import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.GameType;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class GameHandler {

    @Autowired
    BusinessManager businessManager;

    private static Set<Game> games = new HashSet<>();

    public static Game createGame(UserDTO owner, GameType gameType){
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

    public static Game getGameOfUser(UserDTO user) {
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

    public static void updateInviteList(UserDTO userDTO) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessageType.INVITE_LIST);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(getInvites(userDTO)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(userDTO.getId(), webSocketMessage);
    }


    public static void updatePlayers(Game game) {
        game.getPlayers().forEach(player-> updatePlayer(player, game));
    }


    public static void startGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.getOwner().equals(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!GameState.IN_LOBBY.equals(game.getGameState()) || !game.isStartable()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        Set<UserDTO> invitedUsers = game.getInvitedUsers();
        game.setInvitedUsers(new HashSet<>());
        invitedUsers.forEach(GameHandler::updateInviteList);

        game.setGameState(GameState.IN_GAME);
        game.start();

        updatePlayers(game);
    }


    private static void updatePlayer(UserDTO player, Game game) {
        GameDTO gameDTO = game.getGameDTO(player);

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessageType.GAME);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(gameDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WebSocketMessageSender.sendMessage(player.getId(), webSocketMessage);
    }

    public static void updatePlayer(UserDTO player) {
        Game game = getGameOfUser(player);
        if (game != null) {
            updatePlayer(player, game);
        }
    }


    public static Set<UserDTO> getInvites(UserDTO user) {
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

        if (game == null || !GameState.IN_LOBBY.equals(game.getGameState())){
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

    public static void acceptInvite(UserDTO user, UserDTO owner) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(owner);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
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


    public static void restartGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.getOwner().equals(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!GameState.ENDED.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        game.setGameState(GameState.IN_LOBBY);
        game.restart();

        updatePlayers(game);
    }

    public static void move(UserDTO user, String moveJSON) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!GameState.IN_GAME.equals(game.getGameState())){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!game.legal(user, moveJSON)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_BAD_REQUEST);
        }

        game.move(user, moveJSON);

        GameHandler.updatePlayers(game);
    }

}
