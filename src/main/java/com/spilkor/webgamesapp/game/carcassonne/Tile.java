package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.model.dto.Coordinate;

import java.util.HashSet;
import java.util.Set;

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
        this.roads = roads == null ? new HashSet<>() : roads;
        this.cities = cities == null ? new HashSet<>() : cities;
        this.fields = fields == null ? new HashSet<>() : fields;
        this.monasteryPosition = monasteryPosition;
        this.pointOfCompass = pointOfCompass;
        this.meeple = meeple;

        this.roads.forEach(road -> road.setTile(this));
        this.cities.forEach(city -> city.setTile(this));
        this.fields.forEach(field -> field.setTile(this));
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

    public Road getRoad(PointOfCompass pointOfCompass) {
        PointOfCompass actualSide = pointOfCompass.subtract(this.pointOfCompass);
        return roads.stream().filter(road -> road.getSides().contains(actualSide)).findFirst().orElse(null);
    }

    public City getCity(PointOfCompass pointOfCompass) {
        PointOfCompass actualSide = pointOfCompass.subtract(this.pointOfCompass);
        return cities.stream().filter(city -> city.getSides().contains(actualSide)).findFirst().orElse(null);
    }

    public Field getField(HalfSide halfSide) {
        HalfSide actualHalfSide = halfSide.subtract(this.pointOfCompass);
        return fields.stream().filter(field -> field.getHalfSides().contains(actualHalfSide)).findFirst().orElse(null);
    }

    public TileSide getTileSide(PointOfCompass pointOfCompass) {
        if (getRoad(pointOfCompass) != null){
            return TileSide.ROAD;
        } else if(getCity(pointOfCompass) != null){
            return TileSide.CITY;
        } else {
            return TileSide.FIELD;
        }
    }

    public Object getPart(int position) {
        for (Road road: roads){
            if (road.getPosition() == position){
                return road;
            }
        }
        for (City city: cities){
            if (city.getPosition() == position){
                return city;
            }
        }
        for (Field field: fields){
            if (field.getPosition() == position){
                return field;
            }
        }
        return null;
    }
}
