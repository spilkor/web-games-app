package com.spilkor.webgamesapp.game.carcassonne;



import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;
import java.util.Set;

public class TileDTO implements Serializable {

    private Coordinate coordinate;
    private TileID id;
    private PointOfCompass pointOfCompass;
    private Meeple meeple;
    private Set<Integer> legalParts = null;

    public TileDTO() {
    }

    public TileDTO(Tile tile) {
        id = tile.getId();
        coordinate = tile.getCoordinate();
        pointOfCompass = tile.getPointOfCompass();
        meeple = tile.getMeeple();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public TileID getId() {
        return id;
    }

    public void setId(TileID id) {
        this.id = id;
    }

    public PointOfCompass getPointOfCompass() {
        return pointOfCompass;
    }

    public void setPointOfCompass(PointOfCompass pointOfCompass) {
        this.pointOfCompass = pointOfCompass;
    }

    public Meeple getMeeple() {
        return meeple;
    }

    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
    }

    public Set<Integer> getLegalParts() {
        return legalParts;
    }

    public void setLegalParts(Set<Integer> legalParts) {
        this.legalParts = legalParts;
    }
}