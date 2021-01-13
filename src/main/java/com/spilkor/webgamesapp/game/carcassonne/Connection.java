package com.spilkor.webgamesapp.game.carcassonne;

import java.util.HashSet;
import java.util.Set;

public class Connection {

    private Set<TilePart> tileParts = new HashSet<>();

    public Connection(TilePart tilePart_1, TilePart tilePart_2){
        this.tileParts.add(tilePart_1);
        this.tileParts.add(tilePart_2);
    }

    public Set<TilePart> getTileParts() {
        return tileParts;
    }

    public void setTileParts(Set<TilePart> tileParts) {
        this.tileParts = tileParts;
    }
}
