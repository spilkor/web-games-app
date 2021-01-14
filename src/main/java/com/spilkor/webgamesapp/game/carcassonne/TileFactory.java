package com.spilkor.webgamesapp.game.carcassonne;


import java.util.HashSet;
import java.util.Set;

import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.*;
import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.WEST_SOUTH;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.*;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.NORTH;
import static com.spilkor.webgamesapp.game.carcassonne.TileID.TILE_0;


public class TileFactory {

    private TileFactory(){
    }

    public static Tile createTile(TileID tileID, Carcassonne carcassonne){
        switch (tileID){
            case TILE_0:
                return create_Tile_0(carcassonne);
//            case TILE_1:
//                return create_Tile_1();
//            case TILE_2:
//                return create_Tile_2();
//            case TILE_3:
//                return create_Tile_3();
//            case TILE_4:
//                return create_Tile_4();
//            case TILE_5:
//                return create_Tile_5();
//            case TILE_6:
//                return create_Tile_6();
//            case TILE_7:
//                return create_Tile_7();
//            case TILE_8:
//                return create_Tile_8();
//            case TILE_9:
//                return create_Tile_9();
//            case TILE_10:
//                return create_Tile_10();
//            case TILE_11:
//                return create_Tile_11();
//            case TILE_12:
//                return create_Tile_12();
//            case TILE_13:
//                return create_Tile_13();
//            case TILE_14:
//                return create_Tile_14();
//            case TILE_15:
//                return create_Tile_15();
//            case TILE_16:
//                return create_Tile_16();
//            case TILE_17:
//                return create_Tile_17();
//            case TILE_18:
//                return create_Tile_18();
//            case TILE_19:
//                return create_Tile_19();
//            case TILE_20:
//                return create_Tile_20();
//            case TILE_21:
//                return create_Tile_21();
//            case TILE_22:
//                return create_Tile_22();
//            case TILE_23:
//                return create_Tile_23();
        } return null;
    }



    private static Tile create_Tile_0(Carcassonne carcassonne) {
        Set<City> cities = new HashSet<>();
        Set<PointOfCompass> cityPointOfCompasses = new HashSet<>();
        cityPointOfCompasses.add(NORTH);
        City city = new City(cityPointOfCompasses);
        cities.add(city);

        Set<Road> roads = new HashSet<>();
        Set<PointOfCompass> roadPointOfCompasses = new HashSet<>();
        roadPointOfCompasses.add(EAST);
        roadPointOfCompasses.add(WEST);
        Road road = new Road(roadPointOfCompasses);
        roads.add(road);

        Set<Field> fields = new HashSet<>();
        Set<HalfSide> halfSides_1 = new HashSet<>();
        halfSides_1.add(EAST_NORTH);
        halfSides_1.add(WEST_NORTH);
        Field field_1 = new Field(halfSides_1);
        fields.add(field_1);
        Set<HalfSide> halfSides_2 = new HashSet<>();
        halfSides_2.add(EAST_SOUTH);
        halfSides_2.add(SOUTH_EAST);
        halfSides_2.add(SOUTH_WEST);
        halfSides_2.add(WEST_SOUTH);
        Field field_2 = new Field(halfSides_2);
        fields.add(field_2);

        return new Tile(carcassonne, TILE_0, null, roads, cities, fields, null, NORTH, null);
    }

    public Tile create_Tile_1() {
        return null;
    }

    private Tile create_Tile_2() {
        return null;
    }

    private  Tile create_Tile_3() {
        return null;
    }

    private  Tile create_Tile_4() {
        return null;
    }

    private  Tile create_Tile_5() {
        return null;
    }

    private  Tile create_Tile_6() {
        return null;
    }

    private  Tile create_Tile_7() {
        return null;
    }

    private  Tile create_Tile_8() {
        return null;
    }

    private  Tile create_Tile_9() {
        return null;
    }

    private  Tile create_Tile_10() {
        return null;
    }

    private  Tile create_Tile_11() {
        return null;
    }

    private  Tile create_Tile_12() {
        return null;
    }

    private  Tile create_Tile_13() {
        return null;
    }

    private  Tile create_Tile_14() {
        return null;
    }

    private  Tile create_Tile_15() {
        return null;
    }

    private  Tile create_Tile_16() {
        return null;
    }

    private  Tile create_Tile_17() {
        return null;
    }

    private  Tile create_Tile_18() {
        return null;
    }

    private  Tile create_Tile_19() {
        return null;
    }

    private  Tile create_Tile_20() {
        return null;
    }

    private  Tile create_Tile_21() {
        return null;
    }

    private  Tile create_Tile_22() {
        return null;
    }

    private  Tile create_Tile_23() {
        return null;
    }

}
