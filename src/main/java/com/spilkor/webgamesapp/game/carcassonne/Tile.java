package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.Point;

import java.util.HashSet;
import java.util.Set;

public class Tile {

    private PointOfCompass pointOfCompass;

    private Side north;
    private Side east;
    private Side south;
    private Side west;

    private boolean hasMonastery = false;

    private Set<Road> roads = new HashSet<>();
    private Set<City> cities = new HashSet<>();
    private Set<Field> fields = new HashSet<>();

    public PointOfCompass getPointOfCompass() {
        return pointOfCompass;
    }

    public void setPointOfCompass(PointOfCompass pointOfCompass) {
        this.pointOfCompass = pointOfCompass;
    }

    public Side getNorth() {
        return north;
    }

    public void setNorth(Side north) {
        this.north = north;
    }

    public Side getEast() {
        return east;
    }

    public void setEast(Side east) {
        this.east = east;
    }

    public Side getSouth() {
        return south;
    }

    public void setSouth(Side south) {
        this.south = south;
    }

    public Side getWest() {
        return west;
    }

    public void setWest(Side west) {
        this.west = west;
    }

    public boolean isHasMonastery() {
        return hasMonastery;
    }

    public void setHasMonastery(boolean hasMonastery) {
        this.hasMonastery = hasMonastery;
    }

    public Set<Road> getRoads() {
        return roads;
    }

    public void setRoads(Set<Road> roads) {
        this.roads = roads;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public Side getUpperSide() {
        return getUpperSide(pointOfCompass);
    }

    public Side getRightSide() {
        return getRightSide(pointOfCompass);
    }

    public Side getLowerSide() {
        return getLowerSide(pointOfCompass);
    }

    public Side getLeftSide() {
        return getLeftSide(pointOfCompass);
    }

    public Side getUpperSide(PointOfCompass pointOfCompass) {
        switch (pointOfCompass){
            case NORTH:
                return north;
            case EAST:
                return east;
            case SOUTH:
                return south;
            case WEST:
                return west;
            default:
                return null;
        }
    }

    public Side getRightSide(PointOfCompass pointOfCompass) {
        switch (pointOfCompass){
            case NORTH:
                return east;
            case EAST:
                return south;
            case SOUTH:
                return west;
            case WEST:
                return north;
            default:
                return null;
        }
    }

    public Side getLowerSide(PointOfCompass pointOfCompass) {
        switch (pointOfCompass){
            case NORTH:
                return south;
            case EAST:
                return west;
            case SOUTH:
                return north;
            case WEST:
                return east;
            default:
                return null;
        }
    }

    public Side getLeftSide(PointOfCompass pointOfCompass) {
        switch (pointOfCompass){
            case NORTH:
                return west;
            case EAST:
                return north;
            case SOUTH:
                return east;
            case WEST:
                return south;
            default:
                return null;
        }
    }
}
