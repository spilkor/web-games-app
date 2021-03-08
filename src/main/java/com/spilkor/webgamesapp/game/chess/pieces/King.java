package com.spilkor.webgamesapp.game.chess.pieces;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return KING;
    }

    @Override
    public boolean validateMove(Piece[][] table, Position source, Position target) {
        int offsetRow = target.getRow() - source.getRow();
        int offsetColumn = target.getColumn() - source.getColumn();

        Color color = table[source.getRow()][source.getColumn()].getColor();
        boolean isCastling = (offsetRow == 2 || offsetRow == 3) && offsetColumn == 0;

        // TODO
//        if (isCastling) {
//            boolean castleAvailable = !castleUnavailable.contains(color);
//            boolean validRookPosition = false;
//
//            if (offsetRow == 2) {
//                if (BLACK.equals(color)) {
//                    validRookPosition = getChessField(0, 7) != null;
//                } else {
//                    validRookPosition =
//                }
//            }
//
//            return !castleAvailable && validRookPosition;
//        }

        return !(Math.abs(offsetRow) > 1 || Math.abs(offsetColumn) > 1);
    }

}
