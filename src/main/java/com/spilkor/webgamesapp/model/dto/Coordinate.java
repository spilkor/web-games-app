package com.spilkor.webgamesapp.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class Coordinate implements Serializable {

    private Integer x;
    private Integer y;

    public Coordinate(){

    }

    public Coordinate(Integer x){
        this.x = x;
    }

    public Coordinate(Integer x, Integer y){
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
        Coordinate other = (Coordinate) o;
        return Objects.equals(x, other.x) && Objects.equals(y, other.y);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += (x != null ? x.hashCode()*1024 : 0);
        result += (y != null ? y.hashCode() : 0);
        return result;
    }

}
