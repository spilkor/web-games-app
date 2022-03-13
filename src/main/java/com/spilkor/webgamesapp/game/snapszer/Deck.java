package com.spilkor.webgamesapp.game.snapszer;

import com.spilkor.webgamesapp.game.snapszer.enums.Color;
import com.spilkor.webgamesapp.game.snapszer.enums.Figure;
import com.spilkor.webgamesapp.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public Deck() {
        cards.add(new Card(Color.MAKK, Figure.ASZ));
        cards.add(new Card(Color.MAKK, Figure.TIZ));
        cards.add(new Card(Color.MAKK, Figure.KIRALY));
        cards.add(new Card(Color.MAKK, Figure.FELSO));
        cards.add(new Card(Color.MAKK, Figure.ALSO));
        cards.add(new Card(Color.MAKK, Figure.KILENC));

        cards.add(new Card(Color.TOK, Figure.ASZ));
        cards.add(new Card(Color.TOK, Figure.TIZ));
        cards.add(new Card(Color.TOK, Figure.KIRALY));
        cards.add(new Card(Color.TOK, Figure.FELSO));
        cards.add(new Card(Color.TOK, Figure.ALSO));
        cards.add(new Card(Color.TOK, Figure.KILENC));

        cards.add(new Card(Color.KOR, Figure.ASZ));
        cards.add(new Card(Color.KOR, Figure.TIZ));
        cards.add(new Card(Color.KOR, Figure.KIRALY));
        cards.add(new Card(Color.KOR, Figure.FELSO));
        cards.add(new Card(Color.KOR, Figure.ALSO));
        cards.add(new Card(Color.KOR, Figure.KILENC));

        cards.add(new Card(Color.ZOLD, Figure.ASZ));
        cards.add(new Card(Color.ZOLD, Figure.TIZ));
        cards.add(new Card(Color.ZOLD, Figure.KIRALY));
        cards.add(new Card(Color.ZOLD, Figure.FELSO));
        cards.add(new Card(Color.ZOLD, Figure.ALSO));
        cards.add(new Card(Color.ZOLD, Figure.KILENC));
    }

    public Card draw(){
        if (cards.isEmpty()){
            return null;
        }
        Card card = MathUtil.selectRandom(cards);
        cards.remove(card);
        return card;
    }

}
