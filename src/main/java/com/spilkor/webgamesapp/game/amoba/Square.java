package com.spilkor.webgamesapp.game.amoba;

import com.spilkor.webgamesapp.model.dto.Point;

import java.io.Serializable;

public class Square implements Serializable {

    private boolean value;
    private Point position;


    public Square (){

    }

    public Square(int x, int y, boolean value){
        this.value = value;
        this.position = new Point(x, y);
    }


    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}