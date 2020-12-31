package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Position;

import java.io.Serializable;

public class Square implements Serializable {

    private boolean value;
    private Position position;


    public Square (){

    }

    public Square(int x, int y, boolean value){
        this.value = value;
        this.position = new Position(x, y);
    }


    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}