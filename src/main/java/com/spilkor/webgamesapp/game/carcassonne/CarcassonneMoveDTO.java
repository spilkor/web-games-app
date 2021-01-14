package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;

public class CarcassonneMoveDTO implements Serializable {

    private MoveType moveType;
    private Coordinate coordinate;
    private PointOfCompass pointOfCompass;

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public PointOfCompass getPointOfCompass() {
        return pointOfCompass;
    }

    public void setPointOfCompass(PointOfCompass pointOfCompass) {
        this.pointOfCompass = pointOfCompass;
    }
}

