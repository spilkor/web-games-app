package com.spilkor.webgamesapp.game.chess.pieces;


import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Rook extends Piece {

    public Rook(Piece[][] table, Color color) {
        super(table, color);
    }

    @Override
    public PieceType getType() {
        return ROOK;
    }

    @Override
    public boolean validateMove(Position source, Position target) {
        return checkLinearFields(source, target);
    }

}
