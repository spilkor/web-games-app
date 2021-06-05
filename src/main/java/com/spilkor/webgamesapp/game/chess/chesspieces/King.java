package com.spilkor.webgamesapp.game.chess.chesspieces;

import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.ChessPosition;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.Color.BLACK;
import static com.spilkor.webgamesapp.game.chess.enums.Color.WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class King extends ChessPiece implements Castle{

    private boolean hasMoved = false;

    King(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return KING;
    }

    @Override
    public boolean validateMove(ChessCoordinate currentPosition, ChessCoordinate target, ChessPosition chessPosition) {
        if (!super.validateMove(currentPosition, target, chessPosition)){
            return false;
        }

        int offsetRow = target.getRow() - currentPosition.getRow();
        int offsetColumn = target.getColumn() - currentPosition.getColumn();

        if (Math.abs(offsetRow) <= 1 && Math.abs(offsetColumn) <= 1){
            return true;
        }

        if (!hasMoved){
            if (WHITE.equals(color)){
                if (currentPosition.equals(new ChessCoordinate(7, 4))){
                    if (target.equals(new ChessCoordinate(7, 2))){
                        if (chessPosition.getTable()[7][1] == null){
                            if (chessPosition.getTable()[7][2] == null){
                                if (chessPosition.getTable()[7][3] == null){
                                    if (chessPosition.getTable()[7][0] instanceof Rook){
                                        Rook rook = (Rook) chessPosition.getTable()[7][0];
                                        if (WHITE.equals(rook.getColor())){
                                            if(!rook.hasMoved()){
                                                for(int row = 0; row < 8; row++){
                                                    for(int column = 0; column < 8; column++){
                                                        ChessPiece chessPiece = chessPosition.getTable()[row][column];
                                                        if (chessPiece != null && BLACK.equals(chessPiece.getColor())){
                                                            if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(7, 4), chessPosition)){
                                                                return false;
                                                            } else if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(7, 3), chessPosition)){
                                                                return false;
                                                            }
                                                        }
                                                    }
                                                }
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if(target.equals(new ChessCoordinate(7, 6))){
                        if (chessPosition.getTable()[7][5] == null){
                            if (chessPosition.getTable()[7][6] == null){
                                if (chessPosition.getTable()[7][7] instanceof Rook){
                                    Rook rook = (Rook) chessPosition.getTable()[7][7];
                                    if (WHITE.equals(rook.getColor())){
                                        if(!rook.hasMoved()){
                                            for(int row = 0; row < 8; row++){
                                                for(int column = 0; column < 8; column++){
                                                    ChessPiece chessPiece = chessPosition.getTable()[row][column];
                                                    if (chessPiece != null && BLACK.equals(chessPiece.getColor())){
                                                        if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(7, 4), chessPosition)){
                                                            return false;
                                                        } else if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(7, 5), chessPosition)){
                                                            return false;
                                                        }
                                                    }
                                                }
                                            }
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (currentPosition.equals(new ChessCoordinate(0, 4))){
                    if (target.equals(new ChessCoordinate(0, 2))){
                        if (chessPosition.getTable()[0][1] == null){
                            if (chessPosition.getTable()[0][2] == null){
                                if (chessPosition.getTable()[0][3] == null){
                                    if (chessPosition.getTable()[0][0] instanceof Rook){
                                        Rook rook = (Rook) chessPosition.getTable()[0][0];
                                        if (BLACK.equals(rook.getColor())){
                                            if(!rook.hasMoved()){
                                                for(int row = 0; row < 8; row++){
                                                    for(int column = 0; column < 8; column++){
                                                        ChessPiece chessPiece = chessPosition.getTable()[row][column];
                                                        if (chessPiece != null && WHITE.equals(chessPiece.getColor())){
                                                            if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(0, 4), chessPosition)){
                                                                return false;
                                                            } else if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(0, 3), chessPosition)){
                                                                return false;
                                                            }
                                                        }
                                                    }
                                                }
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if(target.equals(new ChessCoordinate(0, 6))){
                        if (chessPosition.getTable()[0][5] == null){
                            if (chessPosition.getTable()[0][6] == null){
                                if (chessPosition.getTable()[0][7] instanceof Rook){
                                    Rook rook = (Rook) chessPosition.getTable()[0][7];
                                    if (BLACK.equals(rook.getColor())){
                                        if(!rook.hasMoved()){
                                            for(int row = 0; row < 8; row++){
                                                for(int column = 0; column < 8; column++){
                                                    ChessPiece chessPiece = chessPosition.getTable()[row][column];
                                                    if (chessPiece != null && WHITE.equals(chessPiece.getColor())){
                                                        if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(0, 4), chessPosition)){
                                                            return false;
                                                        } else if (chessPiece.validateMove(new ChessCoordinate(row, column), new ChessCoordinate(0, 5), chessPosition)){
                                                            return false;
                                                        }
                                                    }
                                                }
                                            }
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
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
