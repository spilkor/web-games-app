package com.spilkor.webgamesapp.game.chess.chesspieces;

import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.ChessPosition;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Bishop extends ChessPiece {

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return BISHOP;
    }

    @Override
    public boolean validateMove(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        return super.validateMove(currentPosition, target, chessPosition) && checkDiagonal(currentPosition, target, chessPosition);
    }
}
