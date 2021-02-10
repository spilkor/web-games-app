package com.spilkor.webgamesapp.game.carcassonne.dto;



import com.spilkor.webgamesapp.game.carcassonne.Meeple;
import com.spilkor.webgamesapp.game.carcassonne.PointOfCompass;
import com.spilkor.webgamesapp.game.carcassonne.Tile;
import com.spilkor.webgamesapp.game.carcassonne.TileID;
import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;
import java.util.Set;

public class TileDTO implements Serializable {

    private Coordinate coordinate;
    private TileID id;
    private PointOfCompass pointOfCompass;
    private Meeple meeple;


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

}