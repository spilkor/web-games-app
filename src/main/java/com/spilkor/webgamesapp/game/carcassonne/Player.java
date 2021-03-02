package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.UserDTO;

public class Player {

    private UserDTO user;
    private Color color;
    private Integer victoryPoints;
    private Integer meeples;
    private Boolean isWinner;

    public Player (UserDTO user, Color color) {
        this.user = user;
        this.color = color;
    }

    public Player (Player player) {
        this.user = player.user;
        this.color = player.color;
        this.victoryPoints = player.victoryPoints;
        this.meeples = player.meeples;
    }

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

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }
}
