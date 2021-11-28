package com.spilkor.webgamesapp.game.snapszer;

import com.spilkor.webgamesapp.game.snapszer.enums.Color;
import com.spilkor.webgamesapp.game.snapszer.enums.Figure;

public class Card {

    private Color color;
    private Figure figure;

    public Card() {
    }

    public Card(Color color, Figure figure) {
        this.color = color;
        this.figure = figure;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card other = (Card) obj;
        return other.getColor() != null && other.getFigure() != null
                && !Color.UNKNOWN.equals(other.getColor()) && !Figure.UNKNOWN.equals(other.getFigure())
                && other.getColor().equals(color) && other.getFigure().equals(figure);
    }
}
