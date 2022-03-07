package com.spilkor.webgamesapp.game.snapszer;

import com.spilkor.webgamesapp.game.snapszer.enums.Color;
import com.spilkor.webgamesapp.game.snapszer.enums.Figure;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private UserDTO user;
    private List<Card> cards = new ArrayList<>();
    private Integer points = null;
    private Boolean withCaller;
    private List<List<Card>> wonRounds = new ArrayList<>();
    private Integer announce;

    public Player(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Boolean getWithCaller() {
        return withCaller;
    }

    public void setWithCaller(Boolean withCaller) {
        this.withCaller = withCaller;
    }

    public List<List<Card>> getWonRounds() {
        return wonRounds;
    }

    public void setWonRounds(List<List<Card>> wonRounds) {
        this.wonRounds = wonRounds;
    }

    public Integer getAnnounce() {
        return announce;
    }

    public void setAnnounce(Integer announce) {
        this.announce = announce;
    }
}
