package com.spilkor.webgamesapp.game.chess.dto;

import com.spilkor.webgamesapp.game.chess.ChessCoordinate;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import java.io.Serializable;

public class ChessMoveDTO implements Serializable {

    private ChessCoordinate source;
    private ChessCoordinate target;
    private PieceType promoteType;
    private boolean draw;

    public ChessCoordinate getSource() {
        return source;
    }

    public void setSource(ChessCoordinate source) {
        this.source = source;
    }

    public ChessCoordinate getTarget() {
        return target;
    }

    public void setTarget(ChessCoordinate target) {
        this.target = target;
    }

    public PieceType getPromoteType() {
        return promoteType;
    }

    public void setPromoteType(PieceType promoteType) {
        this.promoteType = promoteType;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}

