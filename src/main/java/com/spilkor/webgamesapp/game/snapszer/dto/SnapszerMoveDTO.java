package com.spilkor.webgamesapp.game.snapszer.dto;

import com.spilkor.webgamesapp.game.snapszer.Card;
import com.spilkor.webgamesapp.game.snapszer.enums.Act;
import com.spilkor.webgamesapp.game.snapszer.enums.Figure;

import java.io.Serializable;

public class SnapszerMoveDTO implements Serializable {

    private Integer csapIndex;
    private Figure csapFigure;
    private Card calledCard;
    private Act act;
    private Card card;

    public Integer getCsapIndex() {
        return csapIndex;
    }

    public void setCsapIndex(Integer csapIndex) {
        this.csapIndex = csapIndex;
    }

    public Figure getCsapFigure() {
        return csapFigure;
    }

    public void setCsapFigure(Figure csapFigure) {
        this.csapFigure = csapFigure;
    }

    public Card getCalledCard() {
        return calledCard;
    }

    public void setCalledCard(Card calledCard) {
        this.calledCard = calledCard;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}

