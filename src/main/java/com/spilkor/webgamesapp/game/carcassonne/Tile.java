package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.Coordinate;
import com.spilkor.webgamesapp.model.dto.UserDTO;

import java.util.HashSet;
import java.util.Set;

import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.*;

public class Tile {

    private Carcassonne carcassonne;
    private TileID id;
    private Coordinate coordinate;
    private Set<Road> roads;
    private Set<City> cities;
    private Set<Field> fields;
    private Integer monasteryPosition;
    private PointOfCompass pointOfCompass;
    private Meeple meeple;

    public Tile(Carcassonne carcassonne, TileID id, Coordinate coordinate, Set<Road> roads, Set<City> cities, Set<Field> fields, Integer monasteryPosition, PointOfCompass pointOfCompass, Meeple meeple) {
        this.carcassonne = carcassonne;
        this.id = id;
        this.coordinate = coordinate;
        this.roads = roads;
        this.cities = cities;
        this.fields = fields;
        this.monasteryPosition = monasteryPosition;
        this.pointOfCompass = pointOfCompass;
        this.meeple = meeple;

        roads.forEach(road -> road.setTile(this));
    }

    private Object getUpperSide() {
        return null;
    }

    private Object getRightSide() {
        return null;
    }

    private Object getLowerSide() {
        return null;
    }

    private Object getLeftSide() {
        return null;
    }


    public TileID getId() {
        return id;
    }

    public void setId(TileID id) {
        this.id = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
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

    public Integer getMonasteryPosition() {
        return monasteryPosition;
    }

    public void setMonasteryPosition(Integer monasteryPosition) {
        this.monasteryPosition = monasteryPosition;
    }

    public PointOfCompass getPointOfCompass() {
        return pointOfCompass;
    }

    public void setPointOfCompass(PointOfCompass pointOfCompass) {
        this.pointOfCompass = pointOfCompass;
    }

    public Meeple getMeeple() {
        return meeple;
    }

    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
    }

    public Road getRoad(PointOfCompass side) {
        switch (pointOfCompass){
            case NORTH:
                switch (side){
                    case NORTH:
                        return roads.stream().filter(road -> road.getSides().contains(NORTH)).findFirst().orElse(null);
                    case EAST:
                        return roads.stream().filter(road -> road.getSides().contains(EAST)).findFirst().orElse(null);
                    case SOUTH:
                        return roads.stream().filter(road -> road.getSides().contains(SOUTH)).findFirst().orElse(null);
                    case WEST:
                        return roads.stream().filter(road -> road.getSides().contains(WEST)).findFirst().orElse(null);
                }

            case EAST:
                switch (side){
                    case NORTH:
                        return roads.stream().filter(road -> road.getSides().contains(WEST)).findFirst().orElse(null);
                    case EAST:
                        return roads.stream().filter(road -> road.getSides().contains(NORTH)).findFirst().orElse(null);
                    case SOUTH:
                        return roads.stream().filter(road -> road.getSides().contains(EAST)).findFirst().orElse(null);
                    case WEST:
                        return roads.stream().filter(road -> road.getSides().contains(SOUTH)).findFirst().orElse(null);
                }

            case SOUTH:
                switch (side){
                    case NORTH:
                        return roads.stream().filter(road -> road.getSides().contains(SOUTH)).findFirst().orElse(null);
                    case EAST:
                        return roads.stream().filter(road -> road.getSides().contains(WEST)).findFirst().orElse(null);
                    case SOUTH:
                        return roads.stream().filter(road -> road.getSides().contains(NORTH)).findFirst().orElse(null);
                    case WEST:
                        return roads.stream().filter(road -> road.getSides().contains(EAST)).findFirst().orElse(null);
                }

            case WEST:
                switch (side){
                    case NORTH:
                        return roads.stream().filter(road -> road.getSides().contains(EAST)).findFirst().orElse(null);
                    case EAST:
                        return roads.stream().filter(road -> road.getSides().contains(SOUTH)).findFirst().orElse(null);
                    case SOUTH:
                        return roads.stream().filter(road -> road.getSides().contains(WEST)).findFirst().orElse(null);
                    case WEST:
                        return roads.stream().filter(road -> road.getSides().contains(NORTH)).findFirst().orElse(null);
                }

                default:return null;
        }
    }
}
