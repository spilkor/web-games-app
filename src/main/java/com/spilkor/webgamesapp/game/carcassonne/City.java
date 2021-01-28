package com.spilkor.webgamesapp.game.carcassonne;

import java.util.Set;

public class City {

    private Set<PointOfCompass> sides;

    private int position;
    private Tile tile;
    private boolean hasShield;

    public City(Set<PointOfCompass> sides, int position, boolean hasShield) {
        this.sides = sides;
        this.position = position;
        this.hasShield = hasShield;
    }

    public boolean isHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
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
