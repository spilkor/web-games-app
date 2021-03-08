package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return QUEEN;
    }

    @Override
    public boolean validateMove(Piece[][] table, Position source, Position target) {
        if (source.getRow() == target.getRow() || source.getColumn() == target.getColumn()) {
            return checkLinearFields(table, target, source);
        } else {
            return checkDiagonalFields(table, source, target);
        }
    }

}
