package com.spilkor.webgamesapp.game.chess;

import java.io.Serializable;
import java.util.Objects;


public class ChessCoordinate implements Serializable {

    private int row;
    private int column;

    public ChessCoordinate() {
    }

    public ChessCoordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessCoordinate that = (ChessCoordinate) o;
        return row == that.row &&
                column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
