package com.spilkor.webgamesapp.game.carcassonne;



import java.io.Serializable;
import java.util.Set;

public class CarcassonneGameDTO implements Serializable {

    private Player nextPlayer;
    private Set<Player> players;
    private Player winner;
    private Set<TileDTO> tiles;
    private TileDTO tile;
    private MoveType nextMoveType;
    private Set<TilePosition> playableTilePositions;
    private Set<Integer> legalParts;


    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
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