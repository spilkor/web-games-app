package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Bishop extends Piece {

    public Bishop(Piece[][] table, Color color) {
        super(table, color);
    }

    @Override
    public PieceType getType() {
        return BISHOP;
    }

    @Override
    public boolean validateMove(Position source, Position target) {
        return checkDiagonalFields(source, target);
    }

}
