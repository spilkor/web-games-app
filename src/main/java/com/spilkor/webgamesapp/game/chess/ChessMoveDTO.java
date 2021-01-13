package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.model.dto.Point;

import java.io.Serializable;

public class ChessMoveDTO implements Serializable {

    private Point fromPosition;
    private Point toPosition;


    public Point getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Point fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Point getToPosition() {
        return toPosition;
    }

    public void setToPosition(Point toPosition) {
        this.toPosition = toPosition;
    }
}

