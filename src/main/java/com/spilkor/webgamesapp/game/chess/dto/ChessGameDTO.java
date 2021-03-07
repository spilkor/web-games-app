package com.spilkor.webgamesapp.game.chess.dto;


import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.OwnerAs;
import com.spilkor.webgamesapp.game.chess.pieces.Piece;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.io.Serializable;

public class ChessGameDTO implements Serializable {

    private Color ownerColor;
    private PieceDTO[][] table;
    private UserDTO nextPlayer;
    private UserDTO winner;
    private boolean draw;
    private OwnerAs ownerAs;

    public Color getOwnerColor() {
        return ownerColor;
    }

    public void setOwnerColor(Color ownerColor) {
        this.ownerColor = ownerColor;
    }

    public PieceDTO[][] getTable() {
        return table;
    }

    public void setTable(PieceDTO[][] table) {
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

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }
}