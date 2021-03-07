package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Knight extends Piece {

    public Knight(Piece[][] table, Color color) {
        super(table, color);
    }

    @Override
    public PieceType getType() {
        return KNIGHT;
    }

    @Override
    public boolean validateMove(Position source, Position target) {
        int offsetRow = source.getRow() - target.getRow();
        int offsetColumn = source.getColumn() - target.getColumn();

        switch (Math.abs(offsetRow)) {
            case 1:
                return Math.abs(offsetColumn) == 2;
            case 2:
                return Math.abs(offsetColumn) == 1;
            default:
                return false;
        }
    }

}
