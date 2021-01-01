package com.spilkor.webgamesapp.game.chess;



import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.io.Serializable;

public class ChessGameDTO implements Serializable {

    private OwnerAs ownerAs;
    private ChessPiece[][] table;
    private UserDTO nextPlayer;
    private UserDTO startingPlayer;

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }

    public ChessPiece[][] getTable() {
        return table;
    }

    public void setTable(ChessPiece[][] table) {
        this.table = table;
    }

    public UserDTO getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(UserDTO nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public UserDTO getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(UserDTO startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
}