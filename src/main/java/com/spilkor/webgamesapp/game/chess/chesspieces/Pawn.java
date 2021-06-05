package com.spilkor.webgamesapp.game.chess.chesspieces;

import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.ChessPosition;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.Color.WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.PieceType.PAWN;

public class Pawn extends ChessPiece {

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return PAWN;
    }

    @Override
    public boolean validateMove(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        ChessPiece targetPiece = chessPosition.getTable()[target.getRow()][target.getColumn()];
        if (targetPiece != null && color.equals(targetPiece.getColor())){
            return false;
        }

        int offsetRow = target.getRow() - currentPosition.getRow();
        int offsetColumn = target.getColumn() - currentPosition.getColumn();

        int DIRECTION = WHITE.equals(color) ? -1 : 1;
        int STARTING_ROW = WHITE.equals(color) ? 6 : 1;

        if (offsetColumn == 0){
            if (offsetRow == DIRECTION){
                if (targetPiece == null){
                    return true;
                }
            } else if (offsetRow == DIRECTION * 2){
                if(currentPosition.getRow() == STARTING_ROW){
                    if (targetPiece == null){
                        if (chessPosition.getTable()[STARTING_ROW + DIRECTION][currentPosition.getColumn()] == null){
                            return true;
                        }
                    }
                }
            }
        } else if(Math.abs(offsetColumn) == 1){
            if (offsetRow == DIRECTION){
                if (targetPiece != null){
                    return true;
                } else if(chessPosition.getEnPassant() != null && chessPosition.getEnPassant() == target.getColumn()){
                    if (target.getRow() == STARTING_ROW + DIRECTION * 4){
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
