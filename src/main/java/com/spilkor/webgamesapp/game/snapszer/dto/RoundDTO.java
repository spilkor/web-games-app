package com.spilkor.webgamesapp.game.snapszer.dto;

import com.spilkor.webgamesapp.game.snapszer.Card;
import com.spilkor.webgamesapp.game.snapszer.enums.Color;
import com.spilkor.webgamesapp.game.snapszer.enums.RoundState;
import com.spilkor.webgamesapp.game.snapszer.enums.TurnValue;

import java.util.ArrayList;
import java.util.List;

public class RoundDTO {

    private PlayerDTO caller;
    private RoundState roundState;
    private List<Card> csapCards = new ArrayList<>();
    private Card csapCard;
    private Card calledCard;
    private Color adu;
    private boolean snapszer;
    private TurnValue turnValue;
    private TurnDTO turn;
    private List<TurnDTO> turns = new ArrayList<>();
    private List<Integer> scoreBoard = null;
    private boolean firstLicitTurn;

    public PlayerDTO getCaller() {
        return caller;
    }

    public void setCaller(PlayerDTO caller) {
        this.caller = caller;
    }

    public RoundState getRoundState() {
        return roundState;
    }

    public void setRoundState(RoundState roundState) {
        this.roundState = roundState;
    }

    public List<Card> getCsapCards() {
        return csapCards;
    }

    public void setCsapCards(List<Card> csapCards) {
        this.csapCards = csapCards;
    }

    public Card getCsapCard() {
        return csapCard;
    }

    public void setCsapCard(Card csapCard) {
        this.csapCard = csapCard;
    }

    public Card getCalledCard() {
        return calledCard;
    }

    public void setCalledCard(Card calledCard) {
        this.calledCard = calledCard;
    }

    public Color getAdu() {
        return adu;
    }

    public void setAdu(Color adu) {
        this.adu = adu;
    }

    public boolean isSnapszer() {
        return snapszer;
    }

    public void setSnapszer(boolean snapszer) {
        this.snapszer = snapszer;
    }

    public TurnValue getTurnValue() {
        return turnValue;
    }

    public void setTurnValue(TurnValue turnValue) {
        this.turnValue = turnValue;
    }

    public TurnDTO getTurn() {
        return turn;
    }

    public void setTurn(TurnDTO turn) {
        this.turn = turn;
    }

    public List<TurnDTO> getTurns() {
        return turns;
    }

    public void setTurns(List<TurnDTO> turns) {
        this.turns = turns;
    }

    public List<Integer> getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(List<Integer> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public boolean isFirstLicitTurn() {
        return firstLicitTurn;
    }

    public void setFirstLicitTurn(boolean firstLicitTurn) {
        this.firstLicitTurn = firstLicitTurn;
    }
}
