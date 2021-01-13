package com.spilkor.webgamesapp.game.carcassonne;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.Point;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.*;
import static com.spilkor.webgamesapp.game.carcassonne.MoveType.*;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.*;


public class Carcassonne extends Game {

    private Map<Point, Tile> placedTiles = new HashMap<>();
    private Map<UserDTO, Color> playerColors = null;
    private UserDTO nextPlayer;
    private Tile nextTile;
    private MoveType nextMoveType;
    private Set<Point> placeablePoints = null;

    public Carcassonne(UserDTO owner, GameType gameType){
        super(owner, gameType);
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        return false;
    }

    @Override
    public boolean isStartable() {
        return players.size() != 1;
    }

    @Override
    public void start() {
        nextPlayer = owner; //FIXME
        placedTiles = new HashMap<>();
        placedTiles.put(new Point(0,0), createStartingTile());
        playerColors = new HashMap<>();
        nextMoveType = TILE;
        placeablePoints.add(new Point(0, 1));
        placeablePoints.add(new Point(0, -1));
        placeablePoints.add(new Point(1, 0));
        placeablePoints.add(new Point(-1, 0));
    }

    @Override
    public void restart() {
        placedTiles = null;
        playerColors = null;
        nextPlayer = null;
        nextTile = null;
        placeablePoints = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
//        ChessGameDTO chessGameDTO = new ChessGameDTO();
//
//        chessGameDTO.setOwnerAs(ownerAs);
//        chessGameDTO.setTable(table);
//        chessGameDTO.setNextPlayer(nextPlayer);
//        chessGameDTO.setStartingPlayer(startingPlayer);
//
//        try {
//            return Mapper.writeValueAsString(chessGameDTO);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
        try {
            CarcassonneMoveDTO carcassonneMoveDTO = Mapper.readValue(moveJSON, CarcassonneMoveDTO.class);

            if (!nextMoveType.equals(carcassonneMoveDTO.getMoveType())){
                return false;
            }

            if (TILE.equals(nextMoveType)){
                PointOfCompass pointOfCompass = carcassonneMoveDTO.getPointOfCompass();
                if (pointOfCompass == null){
                    return false;
                }

                Point point = carcassonneMoveDTO.getPoint();
                if (point == null || point.getX() == null || point.getY() == null){
                    return false;
                }

                if (!placeablePoints.contains(point)){
                    return false;
                }

                Tile tileAbove = getTile(point.getX() -1, point.getY());
                if (tileAbove != null){
                    if (!nextTile.getUpperSide(pointOfCompass).equals(tileAbove.getLowerSide())){
                        return false;
                    }
                }

                Tile tileToTheRight = getTile(point.getX(), point.getY() + 1);
                if (tileToTheRight != null){
                    if (!nextTile.getRightSide(pointOfCompass).equals(tileToTheRight.getLeftSide())){
                        return false;
                    }
                }

                Tile tileBelow = getTile(point.getX() + 1, point.getY());
                if (tileBelow != null){
                    if (!nextTile.getLowerSide(pointOfCompass).equals(tileBelow.getUpperSide())){
                        return false;
                    }
                }

                Tile tileToTheLeft = getTile(point.getX(), point.getY() -1);
                if (tileToTheLeft != null){
                    if (!nextTile.getLeftSide(pointOfCompass).equals(tileToTheLeft.getRightSide())){
                        return false;
                    }
                }
            } else {
//                MEEPLE
                return false;
            }

        } catch (JsonProcessingException e) {
            return false;
        }

        return true;
    }

    private Tile getTile(int x, int y){
        return placedTiles.get(new Point(x, y));
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {

    }

    private static Tile createStartingTile() {
        Tile startingTile = new Tile();

        Set<City> cities = new HashSet<>();
        Set<PointOfCompass> cityPointOfCompasses = new HashSet<>();
        cityPointOfCompasses.add(NORTH);
        City city = new City(cityPointOfCompasses);
        cities.add(city);
        startingTile.setCities(cities);

        Set<Road> roads = new HashSet<>();
        Set<PointOfCompass> roadPointOfCompasses = new HashSet<>();
        roadPointOfCompasses.add(EAST);
        roadPointOfCompasses.add(WEST);
        Road road = new Road(roadPointOfCompasses);
        roads.add(road);
        startingTile.setRoads(roads);

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
        startingTile.setFields(fields);

        return startingTile;
    }


}
