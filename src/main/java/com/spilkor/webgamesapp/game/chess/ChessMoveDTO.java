package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.model.dto.Position;

import java.io.Serializable;

public class ChessMoveDTO implements Serializable {

    private Position fromPosition;
    private Position toPosition;


    public Position getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Position fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Position getToPosition() {
        return toPosition;
    }

    public void setToPosition(Position toPosition) {
        this.toPosition = toPosition;
    }
}

