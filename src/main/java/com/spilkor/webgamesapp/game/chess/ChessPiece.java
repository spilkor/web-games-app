package com.spilkor.webgamesapp.game.chess;

import java.io.Serializable;

public class ChessPiece implements Serializable {

    private ChessPieceType type;
    private Color color;

    public ChessPiece() {

    }

    public ChessPiece(ChessPieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public ChessPieceType getType() {
        return type;
    }

    public void setType(ChessPieceType type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

