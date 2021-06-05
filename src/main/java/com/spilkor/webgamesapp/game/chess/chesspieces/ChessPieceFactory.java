package com.spilkor.webgamesapp.game.chess.chesspieces;

import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.QUEEN;

public class ChessPieceFactory {

    public static ChessPiece createChessPiece(PieceType pieceType, Color color) {
        switch (pieceType == null ? QUEEN : pieceType) {
            case ROOK:
                return new Rook(color);
            case KNIGHT:
                return new Knight(color);
            case BISHOP:
                return new Bishop(color);
            case KING:
                return new King(color);
            case PAWN:
                return new Pawn(color);
            case QUEEN:
            default:
                return new Queen(color);
        }
    }
}
