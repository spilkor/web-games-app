package com.spilkor.webgamesapp.game.chess.pieces;


import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return ROOK;
    }

    @Override
    public boolean validateMove(Piece[][] table, Position source, Position target) {
        return checkLinearFields(table, target, source);
    }

}
