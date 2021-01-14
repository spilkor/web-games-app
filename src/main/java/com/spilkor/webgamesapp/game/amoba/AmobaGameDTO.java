package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Coordinate;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.io.Serializable;
import java.util.List;

public class AmobaGameDTO implements Serializable {

    private UserDTO nextPlayer;
    private UserDTO winner;
    private OwnerAs ownerAs;
    private AmobaSize amobaSize;
    private Boolean nextSign;
    private Boolean[][] table;
    private List<Square> squares;
    private Coordinate lastPosition;


    public UserDTO getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(UserDTO nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public UserDTO getWinner() {
        return winner;
    }

    public void setWinner(UserDTO winner) {
        this.winner = winner;
    }

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }

    public AmobaSize getAmobaSize() {
        return amobaSize;
    }

    public void setAmobaSize(AmobaSize amobaSize) {
        this.amobaSize = amobaSize;
    }

    public Boolean getNextSign() {
        return nextSign;
    }

    public void setNextSign(Boolean nextSign) {
        this.nextSign = nextSign;
    }

    public Boolean[][] getTable() {
        return table;
    }

    public void setTable(Boolean[][] table) {
        this.table = table;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public Coordinate getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Coordinate lastPosition) {
        this.lastPosition = lastPosition;
    }
}