package com.spilkor.webgamesapp.game.minta;

import com.google.common.collect.Lists;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.util.List;

public class Minta extends Game {

    private List<Player> players = Lists.newArrayList();

    protected Minta(UserDTO owner, GameType gameType) {
        super(owner, gameType);
        players.add(new Player(owner));
    }

    @Override
    public String getGameJSON(UserDTO player) {
        return null;
    }

    @Override
    public boolean isStartable() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void restart() {

    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        return false;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
        return false;
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {

    }

    @Override
    public void surrender(UserDTO userDTO) {

    }
}
