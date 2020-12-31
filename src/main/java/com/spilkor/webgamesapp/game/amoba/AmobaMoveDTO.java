package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Position;

import java.io.Serializable;

public class AmobaMoveDTO implements Serializable {

    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

