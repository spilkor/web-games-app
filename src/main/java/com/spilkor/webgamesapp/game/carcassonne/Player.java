package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.UserDTO;

public class Player {

    public Player (UserDTO user, Color color) {
        this.user = user;
        this.color = color;
    }

    private UserDTO user;
    private Color color;

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
}
