package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.game.chess.dto.PieceDTO;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;
import com.spilkor.webgamesapp.game.chess.chesspieces.*;
import com.spilkor.webgamesapp.util.MathUtil;

import static com.spilkor.webgamesapp.game.chess.chesspieces.ChessPieceFactory.createChessPiece;
import static com.spilkor.webgamesapp.game.chess.enums.Color.BLACK;
import static com.spilkor.webgamesapp.game.chess.enums.Color.WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;


// 00|01|02|03|04|05|06|07
// 10|11|12|13|14|15|16|17
// 20|21|22|23|24|25|26|27
// 30|31|32|33|34|35|36|37
// 40|41|42|43|44|45|46|47
// 50|51|52|53|54|55|56|57
// 60|61|62|63|64|65|66|67
// 70|71|72|73|74|75|76|77

// BR|BK|BB|BQ|BK|BB|BK|BR
// BP|BP|BP|BP|BP|BP|BP|BP
// --|--|--|--|--|--|--|--
// --|--|--|--|--|--|--|--
// --|--|--|--|--|--|--|--
// --|--|--|--|--|--|--|--
// WP|WP|WP|WP|WP|WP|WP|WP
// WR|WK|WB|WQ|WK|WB|WK|WR


public class ChessPosition {

    private final ChessPiece[][] table = new ChessPiece[8][8];
    private ChessCoordinate whiteKingCoordinate;
    private ChessCoordinate blackKingCoordinate;
    private Color nextColor;
    private int numberOfMovesSinceLastPawnMoveOrTake = 0;
    private Integer enPassant;
    private ChessCoordinate promotionCoordinate = null;

    ChessPosition() {
        nextColor = WHITE;
        table[0][0] = createChessPiece(ROOK, BLACK);
        table[0][1] = createChessPiece(KNIGHT, BLACK);
        table[0][2] = createChessPiece(BISHOP, BLACK);
        table[0][3] = createChessPiece(QUEEN, BLACK);
        table[0][4] = createChessPiece(KING, BLACK);
        table[0][5] = createChessPiece(BISHOP, BLACK);
        table[0][6] = createChessPiece(KNIGHT, BLACK);
        table[0][7] = createChessPiece(ROOK, BLACK);
        table[1][0] = createChessPiece(PAWN, BLACK);
        table[1][1] = createChessPiece(PAWN, BLACK);
        table[1][2] = createChessPiece(PAWN, BLACK);
        table[1][3] = createChessPiece(PAWN, BLACK);
        table[1][4] = createChessPiece(PAWN, BLACK);
        table[1][5] = createChessPiece(PAWN, BLACK);
        table[1][6] = createChessPiece(PAWN, BLACK);
        table[1][7] = createChessPiece(PAWN, BLACK);
        table[6][0] = createChessPiece(PAWN, WHITE);
        table[6][1] = createChessPiece(PAWN, WHITE);
        table[6][2] = createChessPiece(PAWN, WHITE);
        table[6][3] = createChessPiece(PAWN, WHITE);
        table[6][4] = createChessPiece(PAWN, WHITE);
        table[6][5] = createChessPiece(PAWN, WHITE);
        table[6][6] = createChessPiece(PAWN, WHITE);
        table[6][7] = createChessPiece(PAWN, WHITE);
        table[7][0] = createChessPiece(ROOK, WHITE);
        table[7][1] = createChessPiece(KNIGHT, WHITE);
        table[7][2] = createChessPiece(BISHOP, WHITE);
        table[7][3] = createChessPiece(QUEEN, WHITE);
        table[7][4] = createChessPiece(KING, WHITE);
        table[7][5] = createChessPiece(BISHOP, WHITE);
        table[7][6] = createChessPiece(KNIGHT, WHITE);
        table[7][7] = createChessPiece(ROOK, WHITE);
        blackKingCoordinate = new ChessCoordinate(0, 4);
        whiteKingCoordinate = new ChessCoordinate(7, 4);
    }

    private ChessPosition(ChessPosition chessPosition) {
        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                ChessPiece chessPiece = chessPosition.table[row][column];
                if (chessPiece != null){
                    ChessPiece copy = createChessPiece(chessPiece.getType(), chessPiece.getColor());
                    if (chessPiece instanceof Castle && ((Castle) chessPiece).hasMoved()){
                        ((Castle) copy).move();
                    }
                    table[row][column] = copy;
                }
            }
        }
        whiteKingCoordinate = new ChessCoordinate(chessPosition.whiteKingCoordinate.getRow(), chessPosition.whiteKingCoordinate.getColumn());
        blackKingCoordinate = new ChessCoordinate(chessPosition.blackKingCoordinate.getRow(), chessPosition.blackKingCoordinate.getColumn());
        nextColor = chessPosition.nextColor;
        numberOfMovesSinceLastPawnMoveOrTake = chessPosition.numberOfMovesSinceLastPawnMoveOrTake;
        enPassant = chessPosition.enPassant;
        promotionCoordinate = chessPosition.promotionCoordinate;
    }

    boolean isKingInCheck(Color kingColor) {
        ChessCoordinate kingCoordinate = WHITE.equals(kingColor) ? whiteKingCoordinate : blackKingCoordinate;
        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                ChessPiece chessPiece = table[row][column];
                if (chessPiece != null){
                    if (!chessPiece.getColor().equals(kingColor)){
                        if (chessPiece.validateMove(new ChessCoordinate(row, column), kingCoordinate, this)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition move(ChessCoordinate source, ChessCoordinate target) {
        ChessPosition chessPositionAfterMove = new ChessPosition(ChessPosition.this);
        chessPositionAfterMove.enPassant = null;

        ChessPiece chessPiece = chessPositionAfterMove.table[source.getRow()][source.getColumn()];
        ChessPiece targetPiece = chessPositionAfterMove.table[target.getRow()][target.getColumn()];

        if (chessPiece instanceof Pawn || targetPiece != null){
            chessPositionAfterMove.numberOfMovesSinceLastPawnMoveOrTake = 0;
        } else {
            chessPositionAfterMove.numberOfMovesSinceLastPawnMoveOrTake++;
        }

        chessPositionAfterMove.table[source.getRow()][source.getColumn()] = null;
        chessPositionAfterMove.table[target.getRow()][target.getColumn()] = chessPiece;

        if (chessPiece instanceof Pawn){
            if (Math.abs(source.getRow() - target.getRow()) == 2){
                chessPositionAfterMove.enPassant = source.getColumn();
            } else if(source.getColumn() != target.getColumn() && targetPiece == null){
                int ROW_OF_OTHER_PAWN = WHITE.equals(chessPiece.getColor()) ? 3 : 4;
                chessPositionAfterMove.getTable()[ROW_OF_OTHER_PAWN][target.getColumn()] = null;
            } else {
                if (WHITE.equals(chessPiece.getColor())){
                    if (target.getRow() == 0){
                        chessPositionAfterMove.promotionCoordinate = target;
                        return chessPositionAfterMove;
                    }
                } else {
                    if (target.getRow() == 7){
                        chessPositionAfterMove.promotionCoordinate = target;
                        return chessPositionAfterMove;
                    }
                }
            }
        } else if (chessPiece instanceof King){
            King king = (King) chessPiece;
            if (WHITE.equals(king.getColor())){
                chessPositionAfterMove.whiteKingCoordinate = target;
                if (!king.hasMoved()){
                    if (target.equals(new ChessCoordinate(7,2))){
                        Rook rook = (Rook) chessPositionAfterMove.table[7][0];
                        rook.move();
                        chessPositionAfterMove.table[7][0] = null;
                        chessPositionAfterMove.table[7][3] = rook;
                    } else if (target.equals(new ChessCoordinate(7,6))){
                        Rook rook = (Rook) chessPositionAfterMove.table[7][7];
                        rook.move();
                        chessPositionAfterMove.table[7][7] = null;
                        chessPositionAfterMove.table[7][5] = rook;
                    }
                }
            } else {
                chessPositionAfterMove.blackKingCoordinate = target;
                if (!king.hasMoved()){
                    if (target.equals(new ChessCoordinate(0,2))){
                        Rook rook = (Rook) chessPositionAfterMove.table[0][0];
                        rook.move();
                        chessPositionAfterMove.table[0][0] = null;
                        chessPositionAfterMove.table[0][3] = rook;
                    } else if (target.equals(new ChessCoordinate(0,6))){
                        Rook rook = (Rook) chessPositionAfterMove.table[0][7];
                        rook.move();
                        chessPositionAfterMove.table[0][7] = null;
                        chessPositionAfterMove.table[0][5] = rook;
                    }
                }
            }
        }

        if (chessPiece instanceof Castle){
            ((Castle) chessPiece).move();
        }

        chessPositionAfterMove.nextColor = WHITE.equals(nextColor) ? BLACK : WHITE;

        return chessPositionAfterMove;
    }


    ChessPosition promote(PieceType promotionType) {
        ChessPiece chessPiece = createChessPiece(promotionType, nextColor);
        if (chessPiece instanceof Rook){
            ((Rook) chessPiece).move();
        }
        table[promotionCoordinate.getRow()][promotionCoordinate.getColumn()] = chessPiece;
        promotionCoordinate = null;
        nextColor = WHITE.equals(nextColor) ? BLACK : WHITE;
        return this;
    }

    public Color getNextColor() {
        return nextColor;
    }

    boolean hasLegalMove() {
        for(int sourceRow = 0; sourceRow < 8; sourceRow++){
            for(int sourceColumn = 0; sourceColumn < 8; sourceColumn++){
                for(int targetRow = 0; targetRow < 8; targetRow++){
                    for(int targetColumn = 0; targetColumn < 8; targetColumn++){
                        if (legal(new ChessCoordinate(sourceRow, sourceColumn), new ChessCoordinate(targetRow, targetColumn))){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean legal(ChessCoordinate source, ChessCoordinate target) {
        if(source.equals(target)){
            return false;
        }

        int sourceRow = source.getRow();
        int sourceColumn = source.getColumn();
        int targetRow = target.getRow();
        int targetColumn = target.getColumn();

        if (!(MathUtil.inRange(0, sourceRow, 7) && MathUtil.inRange(0, sourceColumn, 7) && MathUtil.inRange(0, targetRow, 7) && MathUtil.inRange(0, targetColumn, 7))){
            return false;
        }

        ChessPiece sourceChessPiece = table[sourceRow][sourceColumn];

        if (sourceChessPiece == null){
            return false;
        }

        if (!sourceChessPiece.getColor().equals(nextColor)){
            return false;
        }

        if (!sourceChessPiece.validateMove(source, target, this)){
            return false;
        }

        ChessPosition chessPositionAfterMove = move(source, target);

        return !chessPositionAfterMove.isKingInCheck(nextColor);
    }

    int getNumberOfMovesSinceLastPawnMoveOrTake() {
        return numberOfMovesSinceLastPawnMoveOrTake;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPosition otherChessPosition = (ChessPosition) obj;

        if (!nextColor.equals(otherChessPosition.nextColor)){
            return false;
        }

        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                ChessPiece chessPiece = table[row][column];
                ChessPiece otherChessPiece = otherChessPosition.table[row][column];
                if ((chessPiece == null && otherChessPiece != null) || (chessPiece != null && !chessPiece.equals(otherChessPiece))){
                    return false;
                }
            }
        }

        if ((enPassant == null && otherChessPosition.enPassant != null) || (enPassant != null && !enPassant.equals(otherChessPosition.enPassant))){
            return false;
        }

        return true;
    }

    boolean hasSufficientMaterial() {
        boolean whiteHasEvenBishop = false;
        boolean whiteHasOddBishop = false;
        boolean whiteHasKnight = false;

        boolean blackHasEvenBishop = false;
        boolean blackHasOddBishop = false;
        boolean blackHasKnight = false;

        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                ChessPiece chessPiece = table[row][column];
                if (chessPiece != null){
                    if (PieceType.PAWN.equals(chessPiece.getType())){
                        return true;
                    } else if (PieceType.QUEEN.equals(chessPiece.getType())){
                        return true;
                    } else if (ROOK.equals(chessPiece.getType())){
                        return true;
                    } else if (PieceType.KNIGHT.equals(chessPiece.getType())){
                        if (WHITE.equals(chessPiece.getColor())){
                            if (whiteHasEvenBishop || whiteHasOddBishop){
                                return true;
                            }
                            whiteHasKnight = true;
                        } else {
                            if (blackHasEvenBishop || blackHasOddBishop){
                                return true;
                            }
                            blackHasKnight = true;
                        }
                    } else if (PieceType.BISHOP.equals(chessPiece.getType())){
                        boolean even = (row + column) % 2 == 0;
                        if (WHITE.equals(chessPiece.getColor())){
                            if (whiteHasKnight){
                                return true;
                            } else if (even){
                                if (whiteHasOddBishop){
                                    return true;
                                }
                                whiteHasEvenBishop = true;
                            } else {
                                if (whiteHasOddBishop){
                                    return true;
                                }
                                whiteHasOddBishop = true;
                            }
                        } else {
                            if (blackHasKnight){
                                return true;
                            } else if (even){
                                if (blackHasOddBishop){
                                    return true;
                                }
                                blackHasEvenBishop = true;
                            } else {
                                if (blackHasOddBishop){
                                    return true;
                                }
                                blackHasOddBishop = true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Integer getEnPassant() {
        return enPassant;
    }

    public ChessPiece[][] getTable() {
        return table;
    }

    boolean isWaitingForPromotionType() {
        return promotionCoordinate != null;
    }

    PieceDTO[][] convertTable() {
        PieceDTO[][] result = new PieceDTO[8][8];
        for (int i = 0; i < table.length; i++) {
            for (int k = 0; k < table[i].length; k++) {
                ChessPiece piece = table[i][k];
                if (piece != null) {
                    result[i][k] = new PieceDTO(piece);
                }
            }
        }
        return result;
    }
}

