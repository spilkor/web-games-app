package com.spilkor.webgamesapp.game.chess.dto;


import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.pieces.Piece;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.io.Serializable;

public class ChessGameDTO implements Serializable {

    private Color ownerColor;
    private Piece[][] table;
    private UserDTO nextPlayer;
    private UserDTO winner;
    private boolean draw;

    public Color getOwnerColor() {
        return ownerColor;
    }

    public void setOwnerColor(Color ownerColor) {
        this.ownerColor = ownerColor;
    }

    public Piece[][] getTable() {
        return table;
    }

    public void setTable(Piece[][] table) {
        this.table = table;
    }

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

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}