package com.spilkor.webgamesapp.game.carcassonne.dto;



import com.spilkor.webgamesapp.game.carcassonne.Color;

import java.io.Serializable;
import java.util.Set;

public class CarcassonneLobbyDTO implements Serializable {

    private Long userId;
    private Color color;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}