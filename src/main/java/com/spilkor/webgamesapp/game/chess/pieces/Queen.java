package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Queen extends Piece {

    public Queen(Piece[][] table, Color color) {
        super(table, color);
    }

    @Override
    public PieceType getType() {
        return QUEEN;
    }

    @Override
    public boolean validateMove(Position source, Position target) {
        if (source.getRow() == target.getRow() || source.getColumn() == target.getColumn()) {
            return checkLinearFields(source, target);
        } else {
            return checkDiagonalFields(source, target);
        }
    }

}
