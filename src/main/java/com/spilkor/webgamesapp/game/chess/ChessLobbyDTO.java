package com.spilkor.webgamesapp.game.chess;


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