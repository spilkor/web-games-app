package com.spilkor.webgamesapp.game.snapszer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnapszerGameDTO implements Serializable {

    private List<PlayerDTO> players;
    private PlayerDTO nextPlayer;
    private List<RoundDTO> rounds = new ArrayList<>();
    private RoundDTO round;
    private List<Integer> scoreBoard = new ArrayList<>();

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public PlayerDTO getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(PlayerDTO nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public List<RoundDTO> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundDTO> rounds) {
        this.rounds = rounds;
    }

    public RoundDTO getRound() {
        return round;
    }

    public void setRound(RoundDTO round) {
        this.round = round;
    }

    public List<Integer> getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(List<Integer> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }
}