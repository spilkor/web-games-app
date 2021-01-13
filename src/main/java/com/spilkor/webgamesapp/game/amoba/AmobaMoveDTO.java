package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Point;

import java.io.Serializable;

public class AmobaMoveDTO implements Serializable {

    private Point position;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}

