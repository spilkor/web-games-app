package com.spilkor.webgamesapp.game.chess.dto;


import com.spilkor.webgamesapp.game.chess.enums.OwnerAs;

import java.io.Serializable;

public class ChessLobbyDTO implements Serializable {

    private OwnerAs ownerAs;

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }
}