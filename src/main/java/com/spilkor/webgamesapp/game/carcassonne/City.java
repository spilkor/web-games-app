package com.spilkor.webgamesapp.game.carcassonne;

import java.util.HashSet;
import java.util.Set;

public class City {

    private Set<PointOfCompass> sides;

    public City(Set<PointOfCompass> sides) {
        this.sides = sides;
    }

    public Set<PointOfCompass> getSides() {
        return sides;
    }

    public void setSides(Set<PointOfCompass> sides) {
        this.sides = sides;
    }
}
