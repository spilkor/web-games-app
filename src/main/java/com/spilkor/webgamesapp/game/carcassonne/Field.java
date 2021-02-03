package com.spilkor.webgamesapp.game.carcassonne;

import java.util.Set;

public class Field {

    private Set<HalfSide> halfSides;
    private int position;
    private Tile tile;
    private Set<City> cities;

    public Field(Set<HalfSide> halfSides, Set<City> cities, int position) {
        this.halfSides = halfSides;
        this.cities = cities;
        this.position = position;
    }

    public Set<HalfSide> getHalfSides() {
        return halfSides;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
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
