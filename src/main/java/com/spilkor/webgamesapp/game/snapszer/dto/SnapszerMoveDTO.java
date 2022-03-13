package com.spilkor.webgamesapp.game.snapszer.dto;

import com.spilkor.webgamesapp.game.snapszer.Card;
import com.spilkor.webgamesapp.game.snapszer.enums.Act;
import com.spilkor.webgamesapp.game.snapszer.enums.ActionType;
import com.spilkor.webgamesapp.game.snapszer.enums.Figure;
import com.spilkor.webgamesapp.game.snapszer.enums.Licit;

import java.io.Serializable;

public class SnapszerMoveDTO implements Serializable {

    private Integer csapIndex;
    private Figure csapFigure;
    private Licit licit;
    private Card card;
    private Boolean count;
    private ActionType actionType;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

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

    public Licit getLicit() {
        return licit;
    }

    public void setLicit(Licit licit) {
        this.licit = licit;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Boolean getCount() {
        return count;
    }

    public void setCount(Boolean count) {
        this.count = count;
    }
}

