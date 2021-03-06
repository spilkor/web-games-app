package com.spilkor.webgamesapp.game.carcassonne.dto;



import com.spilkor.webgamesapp.game.carcassonne.MoveType;
import com.spilkor.webgamesapp.game.carcassonne.Player;
import com.spilkor.webgamesapp.game.carcassonne.TilePosition;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CarcassonneGameDTO implements Serializable {

    private Player nextPlayer;
    private Player surrendered;
    private List<Player> players;
    private Set<Player> finalPlayers;
    private Set<TileDTO> tiles;
    private TileDTO tile;
    private MoveType nextMoveType;
    private Set<TilePosition> playableTilePositions;
    private Set<Integer> legalParts;
    private Integer deckSize;

    public Integer getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(Integer deckSize) {
        this.deckSize = deckSize;
    }

    public Player getSurrendered() {
        return surrendered;
    }

    public void setSurrendered(Player surrendered) {
        this.surrendered = surrendered;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Set<Player> getFinalPlayers() {
        return finalPlayers;
    }

    public void setFinalPlayers(Set<Player> finalPlayers) {
        this.finalPlayers = finalPlayers;
    }

    public Set<TileDTO> getTiles() {
        return tiles;
    }

    public void setTiles(Set<TileDTO> tiles) {
        this.tiles = tiles;
    }

    public TileDTO getTile() {
        return tile;
    }

    public void setTile(TileDTO tile) {
        this.tile = tile;
    }

    public MoveType getNextMoveType() {
        return nextMoveType;
    }

    public void setNextMoveType(MoveType nextMoveType) {
        this.nextMoveType = nextMoveType;
    }

    public Set<TilePosition> getPlayableTilePositions() {
        return playableTilePositions;
    }

    public void setPlayableTilePositions(Set<TilePosition> playableTilePositions) {
        this.playableTilePositions = playableTilePositions;
    }

    public Set<Integer> getLegalParts() {
        return legalParts;
    }

    public void setLegalParts(Set<Integer> legalParts) {
        this.legalParts = legalParts;
    }
}