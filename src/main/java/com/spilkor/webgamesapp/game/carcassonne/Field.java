package com.spilkor.webgamesapp.game.carcassonne;

import java.util.HashSet;
import java.util.Set;

public class Field {

    private Set<HalfSide> halfSides = new HashSet<>();

    public Field(Set<HalfSide> halfSides) {
        this.halfSides = halfSides;
    }

    public Set<HalfSide> getHalfSides() {
        return halfSides;
    }

    public void setHalfSides(Set<HalfSide> halfSides) {
        this.halfSides = halfSides;
    }
}
