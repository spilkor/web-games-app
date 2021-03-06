package com.spilkor.webgamesapp.game.carcassonne;


import java.util.HashSet;
import java.util.Set;

import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.*;
import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.WEST_SOUTH;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.*;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.NORTH;
import static com.spilkor.webgamesapp.game.carcassonne.TileID.*;


public class TileFactory {

    private TileFactory(){
    }

    public static Tile createTile(TileID tileID){
        switch (tileID){
            case TILE_0:
                return create_Tile_0();
            case TILE_1:
                return create_Tile_1();
            case TILE_2:
                return create_Tile_2();
            case TILE_3:
                return create_Tile_3();
            case TILE_4:
                return create_Tile_4();
            case TILE_5:
                return create_Tile_5();
            case TILE_6:
                return create_Tile_6();
            case TILE_7:
                return create_Tile_7();
            case TILE_8:
                return create_Tile_8();
            case TILE_9:
                return create_Tile_9();
            case TILE_10:
                return create_Tile_10();
            case TILE_11:
                return create_Tile_11();
            case TILE_12:
                return create_Tile_12();
            case TILE_13:
                return create_Tile_13();
            case TILE_14:
                return create_Tile_14();
            case TILE_15:
                return create_Tile_15();
            case TILE_16:
                return create_Tile_16();
            case TILE_17:
                return create_Tile_17();
            case TILE_18:
                return create_Tile_18();
            case TILE_19:
                return create_Tile_19();
            case TILE_20:
                return create_Tile_20();
            case TILE_21:
                return create_Tile_21();
            case TILE_22:
                return create_Tile_22();
            case TILE_23:
                return create_Tile_23();
                default:return null;
        }
    }

    private static Tile create_Tile_0() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        roadPointOfCompasses_1.add(WEST);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(WEST_NORTH);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1, 2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        halfSides_2.add(SOUTH_WEST);
        halfSides_2.add(WEST_SOUTH);
        Field field_2 = new Field(halfSides_2, new HashSet<>(), 3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_0, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_1() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(NORTH);
        roadPointOfCompasses_1.add(WEST);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        halfSides_1.add(WEST_SOUTH);
        Field field_1 = new Field(halfSides_1, new HashSet<>(), 1);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(WEST_NORTH);
        halfSides_2.add(NORTH_WEST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(), 2);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_1, null, roads, null, fields, null, NORTH, null);
    }

    private static Tile create_Tile_2() {
        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        roadPointOfCompasses_1.add(WEST);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(WEST_NORTH);
        halfSides_1.add(NORTH_WEST);
        Field field_1 = new Field(halfSides_1, new HashSet<>(),1);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        halfSides_2.add(SOUTH_WEST);
        halfSides_2.add(WEST_SOUTH);
        Field field_2 = new Field(halfSides_2, new HashSet<>(),2);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_2, null, roads, null, fields, null, NORTH, null);
    }

    private static Tile create_Tile_3() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(NORTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<PointOfCompass> roadPointOfCompasses_2 = new HashSet<>();
        roadPointOfCompasses_2.add(EAST);
        Road road_2 = new Road(roadPointOfCompasses_2, 1);

        Set<PointOfCompass> roadPointOfCompasses_3 = new HashSet<>();
        roadPointOfCompasses_3.add(SOUTH);
        Road road_3 = new Road(roadPointOfCompasses_3, 2);

        Set<PointOfCompass> roadPointOfCompasses_4 = new HashSet<>();
        roadPointOfCompasses_4.add(WEST);
        Road road_4 = new Road(roadPointOfCompasses_4, 3);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);
        roads.add(road_2);
        roads.add(road_3);
        roads.add(road_4);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(EAST_NORTH);
        Field field_1 = new Field(halfSides_1, new HashSet<>(), 4);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(), 5);

        Set<HalfSide> halfSides_3 = new HashSet<>();
        halfSides_3.add(SOUTH_WEST);
        halfSides_3.add(WEST_SOUTH);
        Field field_3 = new Field(halfSides_3, new HashSet<>(), 6);

        Set<HalfSide> halfSides_4 = new HashSet<>();
        halfSides_4.add(WEST_NORTH);
        halfSides_4.add(NORTH_WEST);
        Field field_4 = new Field(halfSides_4, new HashSet<>(), 7);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);
        fields.add(field_3);
        fields.add(field_4);

        return new Tile(TILE_3, null, roads, null, fields, null, NORTH, null);
    }

    private static Tile create_Tile_4() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<PointOfCompass> roadPointOfCompasses_2 = new HashSet<>();
        roadPointOfCompasses_2.add(SOUTH);
        Road road_2 = new Road(roadPointOfCompasses_2, 1);

        Set<PointOfCompass> roadPointOfCompasses_3 = new HashSet<>();
        roadPointOfCompasses_3.add(WEST);
        Road road_3 = new Road(roadPointOfCompasses_3, 2);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);
        roads.add(road_2);
        roads.add(road_3);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(WEST_NORTH);
        halfSides_1.add(NORTH_WEST);
        Field field_1 = new Field(halfSides_1, new HashSet<>(), 3);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(), 4);

        Set<HalfSide> halfSides_3 = new HashSet<>();
        halfSides_3.add(SOUTH_WEST);
        halfSides_3.add(WEST_SOUTH);
        Field field_3 = new Field(halfSides_3, new HashSet<>(), 5);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);
        fields.add(field_3);

        return new Tile(TILE_4, null, roads, null, fields, null, NORTH, null);
    }

    private static Tile create_Tile_5() {

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        halfSides_1.add(WEST_SOUTH);
        halfSides_1.add(WEST_NORTH);
        halfSides_1.add(NORTH_WEST);
        Field field_1 = new Field(halfSides_1, new HashSet<>(), 0);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_5, null, null, null, fields, 1, NORTH, null);
    }

    private static Tile create_Tile_6() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(SOUTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        halfSides_1.add(WEST_SOUTH);
        halfSides_1.add(WEST_NORTH);
        halfSides_1.add(NORTH_WEST);
        Field field_1 = new Field(halfSides_1, new HashSet<>(), 1);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_6, null, roads, null, fields, 2, NORTH, null);
    }

    private static Tile create_Tile_7() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(SOUTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(NORTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,1);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_7, null, null, cities, fields, null, NORTH, null);
    }


    private static Tile create_Tile_8() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(SOUTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, true);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(NORTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1, 1);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_8, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_9() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(SOUTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, true);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        return new Tile(TILE_9, null, null, cities, null, null, NORTH, null);
    }

    private static Tile create_Tile_10() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,1);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_10, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_11() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, true);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,1);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_11, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_12() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        roadPointOfCompasses_1.add(SOUTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(SOUTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1, 2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(), 3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_12, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_13() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        roadPointOfCompasses_1.add(SOUTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 1, true);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(SOUTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(),3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_13, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_14() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 0, false);

        Set<PointOfCompass> cityPointOfCompasses_2 = new HashSet<>();
        cityPointOfCompasses_2.add(WEST);
        City city_2 = new City(cityPointOfCompasses_2, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);
        cities.add(city_2);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        citiesOfField_1.add(city_2);
        Field field_1 = new Field(halfSides_1, citiesOfField_1, 2);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_14, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_15() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 0, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(SOUTH_WEST);
        halfSides_1.add(WEST_SOUTH);
        halfSides_1.add(WEST_NORTH);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,1);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_15, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_16() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<PointOfCompass> roadPointOfCompasses_2 = new HashSet<>();
        roadPointOfCompasses_2.add(SOUTH);
        Road road_2 = new Road(roadPointOfCompasses_2, 1);

        Set<PointOfCompass> roadPointOfCompasses_3 = new HashSet<>();
        roadPointOfCompasses_3.add(WEST);
        Road road_3 = new Road(roadPointOfCompasses_3, 2);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);
        roads.add(road_2);
        roads.add(road_3);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 3, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(WEST_NORTH);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,4);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(),5);

        Set<HalfSide> halfSides_3 = new HashSet<>();
        halfSides_3.add(SOUTH_WEST);
        halfSides_3.add(WEST_SOUTH);
        Field field_3 = new Field(halfSides_3, new HashSet<>(),6);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);
        fields.add(field_3);

        return new Tile(TILE_16, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_17() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(EAST);
        roadPointOfCompasses_1.add(SOUTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(SOUTH_WEST);
        halfSides_1.add(WEST_SOUTH);
        halfSides_1.add(WEST_NORTH);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        Field field_2 = new Field(halfSides_2, new HashSet<>(),3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_17, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_18() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(SOUTH);
        roadPointOfCompasses_1.add(WEST);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(SOUTH_EAST);
        halfSides_1.add(WEST_NORTH);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(SOUTH_WEST);
        halfSides_2.add(WEST_SOUTH);
        Field field_2 = new Field(halfSides_2, new HashSet<>(),3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_18, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_19() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(NORTH);
        City city_1 = new City(cityPointOfCompasses_1, 0, false);

        Set<PointOfCompass> cityPointOfCompasses_2 = new HashSet<>();
        cityPointOfCompasses_2.add(SOUTH);
        City city_2 = new City(cityPointOfCompasses_2, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);
        cities.add(city_2);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(EAST_SOUTH);
        halfSides_1.add(WEST_SOUTH);
        halfSides_1.add(WEST_NORTH);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        citiesOfField_1.add(city_2);
        Field field_1 = new Field(halfSides_1, citiesOfField_1, 2);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);

        return new Tile(TILE_19, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_20() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(NORTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(SOUTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 1, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(NORTH_WEST);
        Set<City> citiesOfField_2 = new HashSet<>();
        citiesOfField_2.add(city_1);
        Field field_2 = new Field(halfSides_2, citiesOfField_2, 3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_20, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_21() {

        Set<PointOfCompass> roadPointOfCompasses_1 = new HashSet<>();
        roadPointOfCompasses_1.add(NORTH);
        Road road_1 = new Road(roadPointOfCompasses_1, 0);

        Set<Road> roads = new HashSet<>();
        roads.add(road_1);

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(SOUTH);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 1, true);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1, 2);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(NORTH_WEST);
        Set<City> citiesOfField_2 = new HashSet<>();
        citiesOfField_2.add(city_1);
        Field field_2 = new Field(halfSides_2, citiesOfField_2, 3);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_21, null, roads, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_22() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, false);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(NORTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,1);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(SOUTH_EAST);
        halfSides_2.add(SOUTH_WEST);
        Set<City> citiesOfField_2 = new HashSet<>();
        citiesOfField_2.add(city_1);
        Field field_2 = new Field(halfSides_2, citiesOfField_2,2);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_22, null, null, cities, fields, null, NORTH, null);
    }

    private static Tile create_Tile_23() {

        Set<PointOfCompass> cityPointOfCompasses_1 = new HashSet<>();
        cityPointOfCompasses_1.add(EAST);
        cityPointOfCompasses_1.add(WEST);
        City city_1 = new City(cityPointOfCompasses_1, 0, true);

        Set<City> cities = new HashSet<>();
        cities.add(city_1);

        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(NORTH_EAST);
        halfSides_1.add(NORTH_WEST);
        Set<City> citiesOfField_1 = new HashSet<>();
        citiesOfField_1.add(city_1);
        Field field_1 = new Field(halfSides_1, citiesOfField_1,1);

        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(SOUTH_EAST);
        halfSides_2.add(SOUTH_WEST);
        Set<City> citiesOfField_2 = new HashSet<>();
        citiesOfField_2.add(city_1);
        Field field_2 = new Field(halfSides_2, citiesOfField_2,2);

        Set<Field> fields = new HashSet<>();
        fields.add(field_1);
        fields.add(field_2);

        return new Tile(TILE_23, null, null, cities, fields, null, NORTH, null);
    }

}
