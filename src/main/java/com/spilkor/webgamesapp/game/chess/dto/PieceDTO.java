package com.spilkor.webgamesapp.game.chess.dto;

import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;
import com.spilkor.webgamesapp.game.chess.pieces.Piece;

public class PieceDTO {

    private Color color;
    private PieceType pieceType;

    public PieceDTO(Piece piece) {
        this.color = piece.getColor();
        this.pieceType = piece.getType();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
