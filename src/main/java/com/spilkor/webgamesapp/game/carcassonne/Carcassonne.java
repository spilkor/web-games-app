package com.spilkor.webgamesapp.game.carcassonne;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.Coordinate;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.*;
import static com.spilkor.webgamesapp.game.carcassonne.MoveType.MEEPLE;
import static com.spilkor.webgamesapp.game.carcassonne.MoveType.TILE;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.*;
import static com.spilkor.webgamesapp.game.carcassonne.TileID.*;

//    -1|-1  0|-1  1|-1
//    -1|0   0|0   1|0
//    -1|1   0|1   1|1

public class Carcassonne extends Game {

    private Set<Player> players = new HashSet<>();
    private Player nextPlayer;
    private Player winner;
    private Set<Tile> tiles;
    private Tile tile;
    private MoveType nextMoveType;
    private Set<Coordinate> playableCoordinates = null;
    private Deck deck;
    private Set<TilePosition> playableTilePositions = null;
    private Set<Integer> legalParts = null;

    public Carcassonne(UserDTO owner, GameType gameType){
        super(owner, gameType);
        players.add(new Player(owner, geColor(players.size())));
    }

    @Override
    public void playerJoined(UserDTO user) {
        players.add(new Player(user, geColor(players.size())));
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        return false;
    }

    @Override
    public boolean isStartable() {
        return players.size() > 1 && players.size() < 5;
    }

    @Override
    public void start() {
//        nextPlayer = MathUtil.selectRandom(players);
//        FIXME
        nextPlayer = players.stream().filter(p->p.getUser().equals(owner)).findFirst().get();

        tiles = new HashSet<>();
        Tile startingTile = TileFactory.createTile(TILE_0, this);
        startingTile.setCoordinate(new Coordinate(0, 0));

        tiles.add(startingTile);

        playableCoordinates = new HashSet<>();
        playableCoordinates.add(new Coordinate(0, 1));
        playableCoordinates.add(new Coordinate(0, -1));
        playableCoordinates.add(new Coordinate(1, 0));
        playableCoordinates.add(new Coordinate(-1, 0));

        nextMoveType = TILE;

        deck = new Deck();

        tile = TileFactory.createTile(deck.draw(),this);
        playableTilePositions = getPlayableTilePositions();
    }

    @Override
    public void restart() {
        tiles = null;
        nextPlayer = null;
        tile = null;
        playableCoordinates = null;
        legalParts = null;
        playableTilePositions = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        CarcassonneGameDTO carcassonneGameDTO = new CarcassonneGameDTO();

        carcassonneGameDTO.setPlayers(players);

        if (GameState.IN_LOBBY.equals(gameState)){
            //TODO
        } else if (GameState.IN_GAME.equals(gameState)){
            carcassonneGameDTO.setNextPlayer(nextPlayer);
            carcassonneGameDTO.setTiles(tiles.stream().map(TileDTO::new).collect(Collectors.toSet()));
            carcassonneGameDTO.setNextMoveType(nextMoveType);
            carcassonneGameDTO.setTile(new TileDTO(tile));
            if (MoveType.TILE.equals(nextMoveType)){
                if (nextPlayer.getUser().equals(user)){
                    getPlayableTilePositions(); //FIXME nem kell
                    carcassonneGameDTO.setPlayableTilePositions(playableTilePositions);
                }
            } else if (MoveType.MEEPLE.equals(nextMoveType)){
                if (nextPlayer.getUser().equals(user)){
                    getLegalParts(); //FIXME nem kell
                    carcassonneGameDTO.setLegalParts(legalParts);
                }
            }
        } else if (GameState.ENDED.equals(gameState)){
            //TODO
            carcassonneGameDTO.setWinner(winner);
        }

        try {
            return Mapper.writeValueAsString(carcassonneGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }

    private Set<TilePosition> getPlayableTilePositions() {
        Set<TilePosition> playableTilePositions = new HashSet<>();
        for (Coordinate coordinate: playableCoordinates){
            if(isTilePlayable(coordinate, NORTH)){
                playableTilePositions.add(new TilePosition(coordinate, NORTH));
            }
            if(isTilePlayable(coordinate, EAST)){
                playableTilePositions.add(new TilePosition(coordinate, EAST));
            }
            if(isTilePlayable(coordinate, SOUTH)){
                playableTilePositions.add(new TilePosition(coordinate, SOUTH));
            }
            if(isTilePlayable(coordinate, WEST)){
                playableTilePositions.add(new TilePosition(coordinate, WEST));
            }
        }
        return playableTilePositions;
    }

    private boolean isTilePlayable(Coordinate coordinate, PointOfCompass pointOfCompass) {
        if (tile == null || !TILE.equals(nextMoveType)){
            return false;
        }

        Tile tileToNorth = getTile(coordinate.getX(), coordinate.getY() - 1);
        if (tileToNorth != null && !tileToNorth.getTileSide(SOUTH).equals(tile.getTileSide(NORTH.subtract(pointOfCompass)))){
            return false;
        }
        Tile tileToEast = getTile(coordinate.getX() + 1, coordinate.getY());
        if (tileToEast != null && !tileToEast.getTileSide(WEST).equals(tile.getTileSide(EAST.subtract(pointOfCompass)))){
            return false;
        }
        Tile tileToSouth = getTile(coordinate.getX(), coordinate.getY() + 1);
        if (tileToSouth != null && !tileToSouth.getTileSide(NORTH).equals(tile.getTileSide(SOUTH.subtract(pointOfCompass)))){
            return false;
        }
        Tile tileToWest = getTile(coordinate.getX() - 1, coordinate.getY());
        if (tileToWest != null && !tileToWest.getTileSide(EAST).equals(tile.getTileSide(WEST.subtract(pointOfCompass)))){
            return false;
        }

        return true;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
//        try {
//            CarcassonneMoveDTO carcassonneMoveDTO = Mapper.readValue(moveJSON, CarcassonneMoveDTO.class);
//
//            if (!nextMoveType.equals(carcassonneMoveDTO.getMoveType())){
//                return false;
//            }
//
//            if (TILE.equals(nextMoveType)){
//                PointOfCompass pointOfCompass = carcassonneMoveDTO.getPointOfCompass();
//                if (pointOfCompass == null){
//                    return false;
//                }
//
//                Coordinate coordinate = carcassonneMoveDTO.getCoordinate();
//                if (coordinate == null || coordinate.getX() == null || coordinate.getY() == null){
//                    return false;
//                }
//
//                if (!placeableCoordinates.contains(coordinate)){
//                    return false;
//                }
//
//                Tile tileAbove = getTile(coordinate.getX() -1, coordinate.getY());
//                if (tileAbove != null){
//                    if (!tile.getUpperSide().equals(tileAbove.getLowerSide())){
//                        return false;
//                    }
//                }
//
//                Tile tileToTheRight = getTile(coordinate.getX(), coordinate.getY() + 1);
//                if (tileToTheRight != null){
//                    if (!tile.getRightSide().equals(tileToTheRight.getLeftSide())){
//                        return false;
//                    }
//                }
//
//                Tile tileBelow = getTile(coordinate.getX() + 1, coordinate.getY());
//                if (tileBelow != null){
//                    if (!tile.getLowerSide().equals(tileBelow.getUpperSide())){
//                        return false;
//                    }
//                }
//
//                Tile tileToTheLeft = getTile(coordinate.getX(), coordinate.getY() -1);
//                if (tileToTheLeft != null){
//                    if (!tile.getLeftSide().equals(tileToTheLeft.getRightSide())){
//                        return false;
//                    }
//                }
//            } else {
////                MEEPLE
//                return false;
//            }
//
//        } catch (JsonProcessingException e) {
//            return false;
//        }

        return true;
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            CarcassonneMoveDTO carcassonneMoveDTO = Mapper.readValue(moveJSON, CarcassonneMoveDTO.class);

            if (TILE.equals(nextMoveType)){
                Coordinate coordinate = carcassonneMoveDTO.getCoordinate();
                tile.setCoordinate(coordinate);
                tile.setPointOfCompass(carcassonneMoveDTO.getPointOfCompass());
                tiles.add(tile);

                playableCoordinates.remove(coordinate);

                Tile tileToTheNorth = getTile(coordinate.getX(), coordinate.getY() - 1);
                if (tileToTheNorth == null){
                    playableCoordinates.add(new Coordinate(coordinate.getX(), coordinate.getY() -1 ));
                }

                Tile tileToTheEast = getTile(coordinate.getX() + 1, coordinate.getY());
                if (tileToTheEast == null){
                    playableCoordinates.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
                }

                Tile tileToTheSouth = getTile(coordinate.getX(), coordinate.getY() + 1);
                if (tileToTheSouth == null){
                    playableCoordinates.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
                }

                Tile tileToTheWest = getTile(coordinate.getX() - 1, coordinate.getY());
                if (tileToTheWest == null){
                    playableCoordinates.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
                }

                playableTilePositions = null;

                legalParts = getLegalParts();

                nextMoveType = MoveType.MEEPLE;
            } else if (MEEPLE.equals(nextMoveType)){
//                MEEPLE
                Meeple meeple = new Meeple();
                meeple.setPosition(carcassonneMoveDTO.getCoordinate().getX());
                meeple.setColor(nextPlayer.getColor());
                tile.setMeeple(meeple);

                nextMoveType = TILE;

                //FIXME next player
                nextPlayer = players.stream().filter(p-> !p.getUser().equals(nextPlayer.getUser())).findFirst().get();

                legalParts = null;

                TileID tileID = drawIfPossible();

                if (tileID == null){
                    endGame();
                    return;
                }

                tile = TileFactory.createTile(tileID,this);
                playableTilePositions = getPlayableTilePositions();
            }

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void endGame() {
        gameState = GameState.ENDED;
    }

    private TileID drawIfPossible(){
        List<TileID> drawn = new ArrayList<>();
        TileID tileID;

        do {
            tileID = deck.draw();

            if (tileID == null){
                break;
            }

            if (drawn.contains(tileID)){
                drawn.add(tileID);
                continue;
            }

            drawn.add(tileID);

            if (!possibleToPlace(tileID)){
                continue;
            }

            break;
        } while (true);

        TileID finalTileID = tileID;
        drawn.stream().filter(t-> !t.equals(finalTileID)).forEach(t-> deck.put(t));

        return finalTileID;
    }

    private boolean possibleToPlace(TileID tileID) {
//        FIXME
        return true;
    }

    private Color geColor(int numberOfPlayers){
        switch (numberOfPlayers){
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.YELLOW;
            case 3:
            default:
                return Color.GREEN;
        }
    }

    private boolean cityHasMeeple(City city) {
        if (city.getTile().getMeeple() != null && city.getTile().getMeeple().getPosition() == city.getPosition()){
            return true;
        }

        Set<City> checked = new HashSet<>();
        checked.add(city);

        return cityHasMeepleRecursive(city, checked);
    }

    private boolean fieldHasMeeple(Field field) {
        if (field.getTile().getMeeple() != null && field.getTile().getMeeple().getPosition() == field.getPosition()){
            return true;
        }

        Set<Field> checked = new HashSet<>();
        checked.add(field);

        return fieldHasMeepleRecursive(field, checked);
    }

    private boolean fieldHasMeepleRecursive(Field field, Set<Field> checked) {
        for(Field neighborField: getNeighborFields(field)){
            if (!checked.contains(neighborField)){
                Meeple meeple = neighborField.getTile().getMeeple();
                if (meeple != null && meeple.getPosition() == neighborField.getPosition()){
                    return true;
                } else {
                    checked.add(neighborField);
                    if (fieldHasMeepleRecursive(neighborField, checked)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean roadHasMeeple(Road road) {
        if (road.getTile().getMeeple() != null && road.getTile().getMeeple().getPosition() == road.getPosition()){
            return true;
        }

        Set<Road> checked = new HashSet<>();
        checked.add(road);

        return roadHasMeepleRecursive(road, checked);
    }

    private boolean cityHasMeepleRecursive(City city, Set<City> checked) {
        for(City neighborCity: getNeighborCities(city)){
            if (!checked.contains(neighborCity)){
                Meeple meeple = neighborCity.getTile().getMeeple();
                if (meeple != null && meeple.getPosition() == neighborCity.getPosition()){
                    return true;
                } else {
                    checked.add(neighborCity);
                    if (cityHasMeepleRecursive(neighborCity, checked)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean roadHasMeepleRecursive(Road road, Set<Road> checked) {
        for(Road neighborRoad: getNeighborRoads(road)){
            if (!checked.contains(neighborRoad)){
                Meeple meeple = neighborRoad.getTile().getMeeple();
                if (meeple != null && meeple.getPosition() == neighborRoad.getPosition()){
                    return true;
                } else {
                    checked.add(neighborRoad);
                    if (roadHasMeepleRecursive(neighborRoad, checked)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private Set<Field> getNeighborFields(Field field){
        Set<Field> neighborFields = new HashSet<>();

        for (HalfSide fieldHalfSide: field.getHalfSides()){
            switch (field.getTile().getPointOfCompass()){
                case NORTH:
                    switch (fieldHalfSide){
                        case NORTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case NORTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case EAST:
                    switch (fieldHalfSide){
                        case NORTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case NORTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case SOUTH:
                    switch (fieldHalfSide){
                        case NORTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case NORTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case WEST:
                    switch (fieldHalfSide){
                        case NORTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case EAST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(SOUTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_EAST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_NORTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case SOUTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() + 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(WEST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_SOUTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_EAST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case WEST_NORTH: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX(), field.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(NORTH_WEST);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                        case NORTH_WEST: {
                            Tile neighborTile = getTile(field.getTile().getCoordinate().getX() - 1, field.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Field neighborField = neighborTile.getField(EAST_SOUTH);
                                if (neighborField != null){
                                    neighborFields.add(neighborField);
                                }
                            }
                            break;
                        }
                    }
                    break;
            }
        }

        return neighborFields;
    }

    private Set<City> getNeighborCities(City city){
        Set<City> neighborCities = new HashSet<>();

        for (PointOfCompass citySide: city.getSides()){
            switch (city.getTile().getPointOfCompass()){
                case NORTH:
                    switch (citySide){
                        case NORTH: {
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(SOUTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() + 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(WEST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(NORTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() - 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(EAST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case EAST:
                    switch (citySide){
                        case NORTH: {
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() + 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(WEST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(NORTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() - 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(EAST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(SOUTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case SOUTH:
                    switch (citySide){
                        case NORTH: {
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(NORTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() - 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(EAST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(SOUTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() + 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(WEST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case WEST:
                    switch (citySide){
                        case NORTH: {
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() - 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(EAST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(SOUTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX() + 1, city.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(WEST);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(city.getTile().getCoordinate().getX(), city.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                City neighborCity = neighborTile.getCity(NORTH);
                                if (neighborCity != null){
                                    neighborCities.add(neighborCity);
                                }
                            }
                            break;
                        }
                    }
                    break;
            }
        }


        return neighborCities;
    }

    private Set<Road> getNeighborRoads(Road road){
        Set<Road> neighborRoads = new HashSet<>();

        for (PointOfCompass roadSide: road.getSides()){
            switch (road.getTile().getPointOfCompass()){
                case NORTH:
                    switch (roadSide){
                        case NORTH: {
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(SOUTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() + 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(WEST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(NORTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() - 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(EAST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case EAST:
                    switch (roadSide){
                        case NORTH: {
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() + 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(WEST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(NORTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() - 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(EAST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(SOUTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case SOUTH:
                    switch (roadSide){
                        case NORTH: {
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(NORTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() - 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(EAST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(SOUTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() + 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(WEST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case WEST:
                    switch (roadSide){
                        case NORTH: {
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() - 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(EAST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case EAST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() - 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(SOUTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case SOUTH:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX() + 1, road.getTile().getCoordinate().getY());
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(WEST);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                        case WEST:{
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getY() + 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(NORTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                            break;
                        }
                    }
                    break;
            }
        }


        return neighborRoads;
    }

    public Set<Integer> getLegalParts() {
        Set<Integer> result = new HashSet<>();

        tile.getRoads().forEach(road-> {
            if (!roadHasMeeple(road)){
                result.add(road.getPosition());
            }
        });

        tile.getCities().forEach(city-> {
            if (!cityHasMeeple(city)){
                result.add(city.getPosition());
            }
        });

        tile.getFields().forEach(field-> {
            if (!fieldHasMeeple(field)){
                result.add(field.getPosition());
            }
        });

        if (tile.getMonasteryPosition() != null){
            result.add(tile.getMonasteryPosition());
        }

        return result;
    }


    private Tile getTile(int x, int y){
        return tiles.stream().filter(t -> t.getCoordinate().getX().equals(x) && t.getCoordinate().getY().equals(y)).findFirst().orElse(null);
    }


    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public MoveType getNextMoveType() {
        return nextMoveType;
    }

    public void setNextMoveType(MoveType nextMoveType) {
        this.nextMoveType = nextMoveType;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

}
