package com.spilkor.webgamesapp.game.chess.dto;


import com.spilkor.webgamesapp.game.chess.Player;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.DrawReason;
import com.spilkor.webgamesapp.game.chess.enums.OwnerAs;

import java.io.Serializable;
import java.util.List;

public class ChessGameDTO implements Serializable {

    private List<Player> players;
    private Color nextColor;
    private PieceDTO[][] table;
    private Player winner;
    private boolean draw;
    private Player surrendered;
    private OwnerAs ownerAs;
    private DrawReason drawReason;
    private boolean waitingForPromotionType;
    private boolean drawActive;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Color getNextColor() {
        return nextColor;
    }

    public void setNextColor(Color nextColor) {
        this.nextColor = nextColor;
    }

    public PieceDTO[][] getTable() {
        return table;
    }

    public void setTable(PieceDTO[][] table) {
        this.table = table;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public Player getSurrendered() {
        return surrendered;
    }

    public void setSurrendered(Player surrendered) {
        this.surrendered = surrendered;
    }

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }

    public DrawReason getDrawReason() {
        return drawReason;
    }

    public void setDrawReason(DrawReason drawReason) {
        this.drawReason = drawReason;
    }

    public boolean isWaitingForPromotionType() {
        return waitingForPromotionType;
    }

    public void setWaitingForPromotionType(boolean waitingForPromotionType) {
        this.waitingForPromotionType = waitingForPromotionType;
    }

    public boolean isDrawActive() {
        return drawActive;
    }

    public void setDrawActive(boolean drawActive) {
        this.drawActive = drawActive;
    }
}