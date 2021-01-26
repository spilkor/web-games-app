package com.spilkor.webgamesapp.game.carcassonne;

import java.util.HashSet;
import java.util.Set;

public class Field {

    private Set<HalfSide> halfSides;
    private int position;
    private Tile tile;

    public Field(Set<HalfSide> halfSides, int position) {
        this.halfSides = halfSides;
        this.position = position;
    }

    public Set<HalfSide> getHalfSides() {
        return halfSides;
    }

    public void setHalfSides(Set<HalfSide> halfSides) {
        this.halfSides = halfSides;
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
