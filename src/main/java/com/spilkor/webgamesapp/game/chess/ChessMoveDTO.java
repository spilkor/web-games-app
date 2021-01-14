package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;

public class ChessMoveDTO implements Serializable {

    private Coordinate fromPosition;
    private Coordinate toPosition;


    public Coordinate getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Coordinate fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Coordinate getToPosition() {
        return toPosition;
    }

    public void setToPosition(Coordinate toPosition) {
        this.toPosition = toPosition;
    }
}

