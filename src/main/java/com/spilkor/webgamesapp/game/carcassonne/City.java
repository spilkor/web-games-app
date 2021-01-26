package com.spilkor.webgamesapp.game.carcassonne;

import java.util.Set;

public class City {

    private Set<PointOfCompass> sides;

    private int position;
    private Tile tile;

    public City(Set<PointOfCompass> sides, int position) {
        this.sides = sides;
        this.position = position;
    }

    public Set<PointOfCompass> getSides() {
        return sides;
    }

    public void setSides(Set<PointOfCompass> sides) {
        this.sides = sides;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
