package com.spilkor.webgamesapp.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class Point implements Serializable {

    private Integer x;
    private Integer y;

    public Point(){

    }

    public Point(Integer x){
        this.x = x;
    }

    public Point(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point other = (Point) o;
        return Objects.equals(x, other.x) && Objects.equals(y, other.y);
    }
}
