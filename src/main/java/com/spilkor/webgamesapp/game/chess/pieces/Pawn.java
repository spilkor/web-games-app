package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.Color.BLACK;
import static com.spilkor.webgamesapp.game.chess.enums.Color.WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Pawn extends Piece {

    public Pawn(Piece[][] table, Color color) {
        super(table, color);
    }

    @Override
    public PieceType getType() {
        return PAWN;
    }

    @Override
    public boolean validateMove(Position source, Position target) {
        int offsetRow = target.getRow() - source.getRow();
        int offsetColumn = target.getColumn() - source.getColumn();

        Color sourceColor = table[source.getRow()][source.getColumn()].getColor();

        if (WHITE.equals(sourceColor) && offsetRow > 1) {
            return false;
        } else if (BLACK.equals(sourceColor) && offsetRow < -1) {
            return false;
        }

        // move 1 or 2 squares
        if (offsetColumn == 0) {
            if (Math.abs(offsetRow) == 0 || Math.abs(offsetRow) > 2) {
                return false;
            }

            if (Math.abs(offsetRow) == 2) {
                if (BLACK.equals(sourceColor) && source.getRow() != 1) {
                    return false;
                } else if (WHITE.equals(sourceColor) && source.getRow() != 6) {
                    return false;
                }
            }

            for (int i = 1; i <= Math.abs(offsetRow); i++) {
                if (table[source.getRow() + Integer.signum(offsetRow) * i][target.getColumn()] != null) {
                    return false;
                }
            }
        } else {
            Piece targetField = table[target.getRow()][target.getColumn()];
            // en passant
            if (targetField == null) {
                // FIXME
                return false;
            } else {
                // capture
                return Math.abs(offsetColumn) == 1 && Math.abs(offsetRow) == 1;
            }
        }

        return true;
    }

}
