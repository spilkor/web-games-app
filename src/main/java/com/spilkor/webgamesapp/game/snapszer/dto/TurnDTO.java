package com.spilkor.webgamesapp.game.snapszer.dto;

import com.spilkor.webgamesapp.game.snapszer.Card;
import com.spilkor.webgamesapp.game.snapszer.Player;

import java.util.ArrayList;
import java.util.List;

public class TurnDTO {

    private PlayerDTO caller;
    private List<Card> cards = new ArrayList<>();
    private boolean twenty;
    private boolean forty;
    private boolean ended;
    private Player strongestPlayer;

    public PlayerDTO getCaller() {
        return caller;
    }

    public void setCaller(PlayerDTO caller) {
        this.caller = caller;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean isTwenty() {
        return twenty;
    }

    public void setTwenty(boolean twenty) {
        this.twenty = twenty;
    }

    public boolean isForty() {
        return forty;
    }

    public void setForty(boolean forty) {
        this.forty = forty;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public Player getStrongestPlayer() {
        return strongestPlayer;
    }

    public void setStrongestPlayer(Player strongestPlayer) {
        this.strongestPlayer = strongestPlayer;
    }
}
