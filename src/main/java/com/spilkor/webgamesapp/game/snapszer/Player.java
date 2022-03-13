package com.spilkor.webgamesapp.game.snapszer;

import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private UserDTO user;
    private List<Card> cards = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return user.equals(player.getUser());
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }

}
