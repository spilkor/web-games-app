package com.spilkor.webgamesapp.game.chess.chesspieces;

import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.ChessPosition;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

public abstract class ChessPiece {

    protected Color color;

    public ChessPiece(Color color) {
        this.color = color;
    }

    public abstract PieceType getType();

    public boolean validateMove(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition){
        ChessPiece targetPiece = chessPosition.getTable()[target.getRow()][target.getColumn()];
        return targetPiece == null || !targetPiece.getColor().equals(color);
    }

    public Color getColor() {
        return color;
    }

    boolean checkLinear(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        int offsetRow = target.getRow() - currentPosition.getRow();
        int offsetColumn = target.getColumn() - currentPosition.getColumn();

        if (offsetRow == 0) {
            for (int i = 1; i <= Math.abs(offsetColumn) - 1; i++) {
                if (chessPosition.getTable()[currentPosition.getRow()][currentPosition.getColumn() + Integer.signum(offsetColumn) * i] != null) {
                    return false;
                }
            }
            return true;
        } else if (offsetColumn == 0) {
            for (int i = 1; i <= Math.abs(offsetRow) - 1; i++) {
                if (chessPosition.getTable()[currentPosition.getRow() + Integer.signum(offsetRow) * i][target.getColumn()] != null) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    boolean checkDiagonal(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        int offsetRow = target.getRow() - currentPosition.getRow();
        int offsetColumn = target.getColumn() - currentPosition.getColumn();

        if (Math.abs(offsetColumn) != Math.abs(offsetRow)) {
            return false;
        }

        for (int i = 1; i <= Math.abs(offsetRow) - 1; i++) {
            if (chessPosition.getTable()[currentPosition.getRow() + Integer.signum(offsetRow) * i][currentPosition.getColumn() + Integer.signum(offsetColumn) * i] != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece otherChessPiece = (ChessPiece) obj;
        return getType().equals(otherChessPiece.getType()) && getColor().equals(otherChessPiece.getColor());
    }
}
