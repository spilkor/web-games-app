package com.spilkor.webgamesapp.game.snapszer.dto;

import com.spilkor.webgamesapp.game.snapszer.Card;
import com.spilkor.webgamesapp.game.snapszer.enums.Color;
import com.spilkor.webgamesapp.game.snapszer.enums.GameStatus;
import com.spilkor.webgamesapp.game.snapszer.enums.TurnValue;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class SnapszerGameDTO implements Serializable {

    private List<PlayerDTO> players;
    private PlayerDTO caller;
    private PlayerDTO nextPlayer;
    private GameStatus gameStatus;
    private List<Card> csapCards;
    private Integer csapIndex;
    private Color adu;
    private Card calledCard;
    private Boolean snapszer;
    private TurnValue turnValue;
    private Set<Card> round;
    private List<Card> lastRound;

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public PlayerDTO getCaller() {
        return caller;
    }

    public void setCaller(PlayerDTO caller) {
        this.caller = caller;
    }

    public PlayerDTO getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(PlayerDTO nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Card> getCsapCards() {
        return csapCards;
    }

    public void setCsapCards(List<Card> csapCards) {
        this.csapCards = csapCards;
    }

    public Integer getCsapIndex() {
        return csapIndex;
    }

    public void setCsapIndex(Integer csapIndex) {
        this.csapIndex = csapIndex;
    }

    public Color getAdu() {
        return adu;
    }

    public void setAdu(Color adu) {
        this.adu = adu;
    }

    public Card getCalledCard() {
        return calledCard;
    }

    public void setCalledCard(Card calledCard) {
        this.calledCard = calledCard;
    }

    public Boolean getSnapszer() {
        return snapszer;
    }

    public void setSnapszer(Boolean snapszer) {
        this.snapszer = snapszer;
    }

    public TurnValue getTurnValue() {
        return turnValue;
    }

    public void setTurnValue(TurnValue turnValue) {
        this.turnValue = turnValue;
    }

    public Set<Card> getRound() {
        return round;
    }

    public void setRound(Set<Card> round) {
        this.round = round;
    }

    public List<Card> getLastRound() {
        return lastRound;
    }

    public void setLastRound(List<Card> lastRound) {
        this.lastRound = lastRound;
    }
}