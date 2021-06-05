package com.spilkor.webgamesapp.game.chess;

import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.model.dto.UserDTO;

public class Player {

    private UserDTO user;
    private Color color;
    private Boolean isWinner;

    public Player(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }
}
