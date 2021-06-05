package com.spilkor.webgamesapp.game.chess.chesspieces;


import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.ChessPosition;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Rook extends ChessPiece implements Castle {

    private boolean hasMoved = false;

    public Rook(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return ROOK;
    }

    @Override
    public boolean validateMove(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        return super.validateMove(currentPosition, target, chessPosition) && checkLinear(currentPosition, target, chessPosition);
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public void move() {
        hasMoved = true;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && hasMoved == ((Castle)obj).hasMoved();
    }
}
