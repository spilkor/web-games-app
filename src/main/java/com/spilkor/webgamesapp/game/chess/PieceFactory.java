package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;
import com.spilkor.webgamesapp.game.chess.pieces.*;

public class PieceFactory {

    public static Piece create(PieceType pieceType, Color color, Piece[][] table) {
        switch (pieceType) {
            case ROOK:
                return new Rook(table, color);
            case KNIGHT:
                return new Knight(table, color);
            case BISHOP:
                return new Bishop(table, color);
            case QUEEN:
                return new Queen(table, color);
            case KING:
                return new King(table, color);
            case PAWN:
                return new Pawn(table, color);
            default:
                return null;
        }
    }
}
