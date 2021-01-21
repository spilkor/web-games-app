package com.spilkor.webgamesapp.game.carcassonne;



import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;
import java.util.Set;

public class TilePosition implements Serializable {

    private Coordinate coordinate;
    private PointOfCompass pointOfCompass;

    public TilePosition() {
    }

    public TilePosition(Coordinate coordinate, PointOfCompass pointOfCompass) {
        this.coordinate = coordinate;
        this.pointOfCompass = pointOfCompass;
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