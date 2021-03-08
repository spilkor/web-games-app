package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import java.util.Arrays;
import java.util.Objects;

public abstract class Piece {

    protected Color color;

    public abstract PieceType getType();
    public abstract boolean validateMove(Piece[][] table, Position source, Position target);

    public Piece(Color color) {
        this.color = color;
    }

    protected boolean checkLinearFields(Piece[][] table, Position target, Position source) {
        int offsetRow = target.getRow() - source.getRow();
        int offsetColumn = target.getColumn() - source.getColumn();

        if (offsetRow == 0) {
            for (int i = 1; i <= Math.abs(offsetColumn) - 1; i++) {
                if (table[source.getRow()][source.getColumn() + Integer.signum(offsetColumn) * i] != null) {
                    return false;
                }
            }
        } else if (offsetColumn == 0) {
            for (int i = 1; i <= Math.abs(offsetRow) - 1; i++) {
                if (table[source.getRow() + Integer.signum(offsetRow) * i][target.getColumn()] != null) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    protected boolean checkDiagonalFields(Piece[][] table, Position source, Position target) {
        int offsetRow = target.getRow() - source.getRow();
        int offsetColumn = target.getColumn() - source.getColumn();

        if (Math.abs(offsetColumn) != Math.abs(offsetRow)) {
            return false;
        }

        for (int i = 1; i <= Math.abs(offsetRow) - 1; i++) {
            if (table[source.getRow() + Integer.signum(offsetRow) * i][source.getColumn() + Integer.signum(offsetColumn) * i] != null) {
                return false;
            }
        }

        return true;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
