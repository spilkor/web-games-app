package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;

public class AmobaMoveDTO implements Serializable {

    private Coordinate position;

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
}

