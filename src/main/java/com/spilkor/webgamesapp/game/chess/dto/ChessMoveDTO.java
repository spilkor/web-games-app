package com.spilkor.webgamesapp.game.chess.dto;

import com.spilkor.webgamesapp.game.chess.Position;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;

import java.io.Serializable;

public class ChessMoveDTO implements Serializable {

    private Position source;
    private Position target;
    private PieceType promoteType;

    public Position getSource() {
        return source;
    }

    public void setSource(Position source) {
        this.source = source;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    public PieceType getPromoteType() {
        return promoteType;
    }

    public void setPromoteType(PieceType promoteType) {
        this.promoteType = promoteType;
    }
}

