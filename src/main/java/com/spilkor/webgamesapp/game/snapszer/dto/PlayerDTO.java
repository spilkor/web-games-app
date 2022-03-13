package com.spilkor.webgamesapp.game.snapszer.dto;

import com.spilkor.webgamesapp.game.snapszer.Card;
import com.spilkor.webgamesapp.game.snapszer.enums.Color;
import com.spilkor.webgamesapp.game.snapszer.enums.Figure;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayerDTO {

    private UserDTO user;
    private List<Card> cards = new ArrayList<>();
    private Boolean withCaller;

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

    public Boolean getWithCaller() {
        return withCaller;
    }

    public void setWithCaller(Boolean withCaller) {
        this.withCaller = withCaller;
    }
}
