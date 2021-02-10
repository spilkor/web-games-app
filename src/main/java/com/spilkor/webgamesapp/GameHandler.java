package com.spilkor.webgamesapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.game.amoba.Amoba;
import com.spilkor.webgamesapp.game.carcassonne.Carcassonne;
import com.spilkor.webgamesapp.game.chess.Chess;
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
                game = new Chess(owner, gameType);
                break;
            case CARCASSONNE:
                game = new Carcassonne(owner, gameType);
                break;
        }

        games.add(game);

        game.unLock();

        return game;
    }

    static Game getGameOfUser(UserDTO user) {
        return games.stream().filter(game -> game.getPlayers().contains(user)).findFirst().orElse(null);
    }

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

    static void surrender(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!Game.GameState.IN_GAME.equals(game.getGameState())){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        game.surrender(user);

        game.unLock();

        updatePlayers(game);
    }

    static void startGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!game.getOwner().equals(user)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!Game.GameState.IN_LOBBY.equals(game.getGameState()) || !game.isStartable()){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        game.setGameState(Game.GameState.IN_GAME);
        game.start();

        game.unLock();

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

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!Game.GameState.IN_LOBBY.equals(game.getGameState())){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!ServiceHelper.getService(BusinessManager.class).friends(user.getId(), friend.getId()) || !game.getOwner().getId().equals(user.getId())){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!game.getInvitedUsers().add(friend)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FOUND);
        }

        game.getInvitedUsers().add(friend);

        game.unLock();

        updateInviteList(user);
        updateInviteList(friend);
        updatePlayers(game);
    }

    static void acceptInvite(UserDTO user, UserDTO owner) throws WebGamesApi.WebGamesApiException {
        Game gameOfOwner = getGameOfUser(owner);

        if (gameOfOwner == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!Game.GameState.IN_LOBBY.equals(gameOfOwner.getGameState()) || !gameOfOwner.getOwner().equals(owner) || !gameOfOwner.getInvitedUsers().contains(user)){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        Game gameOfUser = GameHandler.getGameOfUser(user);

        if (gameOfUser != null){

            if (!gameOfUser.lock()){
                throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
            }

            if (Game.GameState.IN_GAME.equals(gameOfUser.getGameState())){
                gameOfUser.unLock();
                throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
            }

        }

        if (!gameOfOwner.lock()){
            if (gameOfUser != null){
                gameOfUser.unLock();
            }
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!Game.GameState.IN_LOBBY.equals(gameOfOwner.getGameState()) || !gameOfOwner.getOwner().equals(owner) || !gameOfOwner.getInvitedUsers().contains(user)){
            gameOfOwner.unLock();
            if (gameOfUser != null){
                gameOfUser.unLock();
            }
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (gameOfUser != null){
            leaveGame(user);
        }
        gameOfOwner.getInvitedUsers().remove(user);
        gameOfOwner.getPlayers().add(user);
        gameOfOwner.playerJoined(user);

        gameOfOwner.unLock();
        if (gameOfUser != null){
            gameOfUser.unLock();
        }

        updateInviteList(user);
        updatePlayers(gameOfOwner);
    }

    static void declineInvite(UserDTO user, UserDTO owner) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(owner);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!game.getInvitedUsers().contains(user)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        game.getInvitedUsers().remove(user);

        game.unLock();

        updateInviteList(user);
        updatePlayers(game);
    }

    static void restartGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!game.getOwner().equals(user)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!Game.GameState.ENDED.equals(game.getGameState())){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        game.setGameState(Game.GameState.IN_LOBBY);
        game.restart();

        game.unLock();

        updatePlayers(game);
    }

    public static void move(UserDTO user, String moveJSON) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!Game.GameState.IN_GAME.equals(game.getGameState())){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!game.legal(user, moveJSON)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_BAD_REQUEST);
        }

        game.move(user, moveJSON);

        game.unLock();

        GameHandler.updatePlayers(game);
    }

    public static void leaveGame(UserDTO user) throws WebGamesApi.WebGamesApiException {
        Game game = getGameOfUser(user);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (Game.GameState.IN_GAME.equals(game.getGameState())){
            game.unLock();
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
        game.playerLeft(user);

        game.unLock();

        updatePlayers(game);
        updatePlayer(user);
    }

    private static void removeGame(Game game) {
        games.remove(game);
        game.lock();

        game.getPlayers().forEach(GameHandler::updatePlayer);
        game.getInvitedUsers().forEach(GameHandler::updateInviteList);
    }

    public static void kickPlayer(UserDTO owner, UserDTO player) throws WebGamesApi.WebGamesApiException  {
        Game game = getGameOfUser(owner);

        if (game == null){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!game.lock()){
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_CONFLICT);
        }

        if (!game.getOwner().equals(owner)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (Game.GameState.IN_GAME.equals(game.getGameState())){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (owner.equals(player)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!game.getPlayers().remove(player)){
            game.unLock();
            throw new WebGamesApi.WebGamesApiException(HttpServletResponse.SC_NOT_FOUND);
        }

        game.playerLeft(player);

        game.unLock();

        updatePlayers(game);
        updatePlayer(player);
    }

}
