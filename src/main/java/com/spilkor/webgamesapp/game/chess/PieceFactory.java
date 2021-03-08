package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;
import com.spilkor.webgamesapp.game.chess.pieces.*;

public class PieceFactory {

    public static Piece create(PieceType pieceType, Color color) {
        switch (pieceType) {
            case ROOK:
                return new Rook(color);
            case KNIGHT:
                return new Knight(color);
            case BISHOP:
                return new Bishop(color);
            case QUEEN:
                return new Queen(color);
            case KING:
                return new King(color);
            case PAWN:
                return new Pawn(color);
            default:
                return null;
        }
    }
}
