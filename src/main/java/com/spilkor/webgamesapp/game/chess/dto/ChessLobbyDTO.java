package com.spilkor.webgamesapp.game.chess.dto;


import com.spilkor.webgamesapp.game.chess.enums.Color;

import java.io.Serializable;

public class ChessLobbyDTO implements Serializable {

    // null means random
    private Color ownerColor;

    public Color getOwnerColor() {
        return ownerColor;
    }

    public void setOwnerColor(Color ownerColor) {
        this.ownerColor = ownerColor;
    }
}