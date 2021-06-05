package com.spilkor.webgamesapp.game.chess.chesspieces;

import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.ChessPosition;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Knight extends ChessPiece {

    public Knight(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return KNIGHT;
    }

    @Override
    public boolean validateMove(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        int offsetRow = target.getRow() - currentPosition.getRow();
        int offsetColumn = target.getColumn() - currentPosition.getColumn();
        return super.validateMove(currentPosition, target, chessPosition) && Math.abs(offsetRow * offsetColumn) == 2;
    }

}
