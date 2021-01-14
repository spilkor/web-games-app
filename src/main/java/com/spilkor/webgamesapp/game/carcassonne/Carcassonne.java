package com.spilkor.webgamesapp.game.carcassonne;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.Coordinate;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.spilkor.webgamesapp.game.carcassonne.MoveType.TILE;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.SOUTH;
import static com.spilkor.webgamesapp.game.carcassonne.TileID.TILE_0;


public class Carcassonne extends Game {

    private Set<Player> players = new HashSet<>();
    private Player nextPlayer;
    private Player winner;
    private Set<Tile> tiles;
    private Tile tile;
    private MoveType nextMoveType;
    private Set<Coordinate> placeableCoordinates = null;
    private Deck deck;

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
        nextPlayer = MathUtil.selectRandom(players);

        tiles = new HashSet<>();
        Tile startingTile = TileFactory.createTile(TILE_0, this);
        startingTile.setCoordinate(new Coordinate(0, 0));
        tiles.add(startingTile);

        placeableCoordinates = new HashSet<>();
        placeableCoordinates.add(new Coordinate(0, 1));
        placeableCoordinates.add(new Coordinate(0, -1));
        placeableCoordinates.add(new Coordinate(1, 0));
        placeableCoordinates.add(new Coordinate(-1, 0));

        nextMoveType = TILE;

        createDeck();

        tile = TileFactory.createTile(deck.draw(),this);
    }

    @Override
    public void restart() {
        tiles = null;
        nextPlayer = null;
        tile = null;
        placeableCoordinates = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        CarcassonneGameDTO carcassonneGameDTO = new CarcassonneGameDTO();

        carcassonneGameDTO.setPlayers(players);

        if (GameState.IN_GAME.equals(gameState)){
            carcassonneGameDTO.setNextPlayer(nextPlayer);
            carcassonneGameDTO.setTiles(tiles.stream().map((Tile t) -> convertTile(t, user)).collect(Collectors.toSet()));
            carcassonneGameDTO.setTile(convertTile(tile, user));
            carcassonneGameDTO.setNextMoveType(nextMoveType);
        } else if (GameState.ENDED.equals(gameState)){
            carcassonneGameDTO.setWinner(winner);
        }

        try {
            return Mapper.writeValueAsString(carcassonneGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

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
        //TODO
    }

    private void createDeck() {
        deck = new Deck();
        deck.put(TILE_0);
        deck.put(TILE_0);
        deck.put(TILE_0);
        //TODO
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


    private boolean roadHasMeeple(Road road) {
        if (road.getTile().getMeeple() != null && road.getTile().getMeeple().getPosition() == road.getPosition()){
            return true;
        }

        Set<Road> checked = new HashSet<>();
        checked.add(road);

        return roadHasMeepleRecursive(road, checked);
    }

    private boolean roadHasMeepleRecursive(Road road, Set<Road> checked) {
        for(Road neighborRoad: getNeighborRoads(road)){
            if (!checked.contains(neighborRoad)){
                Meeple meeple = neighborRoad.getTile().getMeeple();
                if (meeple != null && meeple.getPosition() == neighborRoad.getPosition()){
                    return true;
                } else {
                    checked.add(neighborRoad);
                    roadHasMeepleRecursive(neighborRoad, checked);
                }
            }
        }
        return false;
    }

    private Set<Road> getNeighborRoads(Road road){
        Set<Road> neighborRoads = new HashSet<>();

        for (PointOfCompass pointOfCompassSide: road.getSides()){
            switch (road.getTile().getPointOfCompass()){
                case NORTH:
                    switch (pointOfCompassSide){
                        case NORTH:
                            Tile neighborTile = getTile(road.getTile().getCoordinate().getX(), road.getTile().getCoordinate().getX() - 1);
                            if (neighborTile != null){
                                Road neighborRoad = neighborTile.getRoad(SOUTH);
                                if (neighborRoad != null){
                                    neighborRoads.add(neighborRoad);
                                }
                            }
                    }
            }
        }


        return neighborRoads;
    }



    private TileDTO convertTile(Tile tile, UserDTO user){
        TileDTO tileDTO = new TileDTO(tile);
        tileDTO.setLegalParts(getLegalParts(tile, user));
        return tileDTO;
    }

    public Set<Integer> getLegalParts(Tile tile, UserDTO user) {
        if (!nextPlayer.getUser().equals(user) || !MoveType.MEEPLE.equals(nextMoveType) || this.tile != tile){
            return null;
        }

        Set<Integer> result = new HashSet<>();
        tile.getRoads().forEach(road-> {
            if (!roadHasMeeple(road)){
                result.add(road.getPosition());
            }
        });
//        tile.getCities().forEach(city-> {
//            if (city.legal()){
//                result.add(city.getPosition());
//            }
//        });
//        tile.getFields().forEach(field-> {
//            if (field.legal()){
//                result.add(field.getPosition());
//            }
//        });
        if (tile.getMonasteryPosition() != null){
            result.add(tile.getMonasteryPosition());
        }

        return result;
    }


    private Tile getTile(int x, int y){
        return tiles.stream().filter(t -> t.getCoordinate().getX().equals(x) && t.getCoordinate().getX().equals(x)).findFirst().orElse(null);
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
