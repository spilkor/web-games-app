package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.UserDTO;

public class Player {

    public Player (UserDTO user, Color color) {
        this.user = user;
        this.color = color;
    }

    private UserDTO user;
    private Color color;
    private Integer victoryPoints;
    private Integer meeples;

    public Integer getMeeples() {
        return meeples;
    }

    public void setMeeples(Integer meeples) {
        this.meeples = meeples;
    }

    public Integer getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(Integer victoryPoints) {
        this.victoryPoints = victoryPoints;
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
}
