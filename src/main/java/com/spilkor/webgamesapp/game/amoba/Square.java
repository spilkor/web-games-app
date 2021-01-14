package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.io.Serializable;

public class Square implements Serializable {

    private boolean value;
    private Coordinate position;


    public Square (){

    }

    public Square(int x, int y, boolean value){
        this.value = value;
        this.position = new Coordinate(x, y);
    }


    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
}