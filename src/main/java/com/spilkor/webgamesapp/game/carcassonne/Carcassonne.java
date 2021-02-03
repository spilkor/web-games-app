package com.spilkor.webgamesapp.game.carcassonne;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.Coordinate;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.spilkor.webgamesapp.game.carcassonne.HalfSide.*;
import static com.spilkor.webgamesapp.game.carcassonne.MoveType.MEEPLE;
import static com.spilkor.webgamesapp.game.carcassonne.MoveType.TILE;
import static com.spilkor.webgamesapp.game.carcassonne.PointOfCompass.*;
import static com.spilkor.webgamesapp.game.carcassonne.TileID.TILE_0;

//    -1|-1  0|-1  1|-1
//    -1|0   0|0   1|0
//    -1|1   0|1   1|1

public class Carcassonne extends Game {

    private List<Player> players = new ArrayList<>();
    private Player nextPlayer;
    private Set<Player> winners;
    private Set<Tile> tiles;
    private Tile tile;
    private MoveType nextMoveType;
    private Set<Coordinate> playableCoordinates = null;
    private Deck deck;
    private Set<TilePosition> playableTilePositions = null;
    private Set<Integer> legalParts = null;
    private Player surrendered = null;

    public Carcassonne(UserDTO owner, GameType gameType){
        super(owner, gameType);
        players.add(new Player(owner, geColor(players.size())));
    }

    @Override
    public void playerJoined(UserDTO user) {
        players.add(new Player(user, geColor(players.size())));
    }

    @Override
    public void playerLeft(UserDTO user) {
        players.remove(players.stream().filter(p-> p.getUser().equals(user)).findFirst().get());
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        try {
            CarcassonneLobbyDTO carcassonneLobbyDTO = Mapper.readValue(lobbyJSON, CarcassonneLobbyDTO.class);

            if (carcassonneLobbyDTO.getColor() == null || carcassonneLobbyDTO.getUserId() == null){
                return false;
            }

            Player player = players.stream().filter(p-> p.getUser().getId().equals(carcassonneLobbyDTO.getUserId())).findFirst().orElse(null);
            if (player == null){
                return false;
            }

            player.setColor(carcassonneLobbyDTO.getColor());

            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void surrender(UserDTO userDTO) {
        surrendered = players.stream().filter(player -> player.getUser().equals(userDTO)).findFirst().get();
        gameState = GameState.ENDED;
    }

    @Override
    public boolean isStartable() {
        return
                players.size() > 1
                        &&
                players.stream().map(Player::getColor).collect(Collectors.toList()).size() ==
                players.stream().map(Player::getColor).collect(Collectors.toSet()).size()
                        &&
                players.stream().noneMatch(player -> player.getColor() == null);
    }

    @Override
    public void start() {
        List<Player> randomOrderedPlayers = new ArrayList<>();
        for(int x = 0; x < players.size() + 1; x ++){
            Player player = MathUtil.selectRandom(players);
            randomOrderedPlayers.add(player);
            players.remove(player);
        }
        players = randomOrderedPlayers;

        players.forEach(player -> {player.setMeeples(8); player.setVictoryPoints(0);});

        nextPlayer = players.get(0);

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
        playableTilePositions = getPlayableTilePositions(tile);
    }

    @Override
    public void restart() {
        tiles = null;
        nextPlayer = null;
        tile = null;
        playableCoordinates = null;
        legalParts = null;
        playableTilePositions = null;
        winners = null;
        nextMoveType = null;
        deck = null;
        surrendered = null;
        players.forEach(player-> {player.setMeeples(null); player.setVictoryPoints(null);});
    }

    @Override
    public String getGameJSON(UserDTO user) {
        CarcassonneGameDTO carcassonneGameDTO = new CarcassonneGameDTO();

        carcassonneGameDTO.setPlayers(players);

        if (GameState.IN_LOBBY.equals(gameState)){

        } else if (GameState.IN_GAME.equals(gameState) || GameState.ENDED.equals(gameState)){
            carcassonneGameDTO.setNextPlayer(nextPlayer);
            carcassonneGameDTO.setTiles(tiles.stream().map(TileDTO::new).collect(Collectors.toSet()));
            carcassonneGameDTO.setNextMoveType(nextMoveType);
            carcassonneGameDTO.setTile(new TileDTO(tile));
            if (MoveType.TILE.equals(nextMoveType)){
                if (nextPlayer.getUser().equals(user)){
                    carcassonneGameDTO.setPlayableTilePositions(playableTilePositions);
                }
            } else if (MoveType.MEEPLE.equals(nextMoveType)){
                if (nextPlayer.getUser().equals(user)){
                    carcassonneGameDTO.setLegalParts(legalParts);
                }
            }
            carcassonneGameDTO.setDeckSize(deck.getSize());
        }

        if (GameState.ENDED.equals(gameState)){
            carcassonneGameDTO.setWinners(winners);
            carcassonneGameDTO.setSurrendered(surrendered);
            carcassonneGameDTO.setDeckSize(deck.getSize());
        }

        try {
            return Mapper.writeValueAsString(carcassonneGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }

    private Set<TilePosition> getPlayableTilePositions(Tile tileToPlay) {
        Set<TilePosition> playableTilePositions = new HashSet<>();
        for (Coordinate coordinate: playableCoordinates){
            if(isTilePlayable(tileToPlay, coordinate, NORTH)){
                playableTilePositions.add(new TilePosition(coordinate, NORTH));
            }
            if(isTilePlayable(tileToPlay, coordinate, EAST)){
                playableTilePositions.add(new TilePosition(coordinate, EAST));
            }
            if(isTilePlayable(tileToPlay, coordinate, SOUTH)){
                playableTilePositions.add(new TilePosition(coordinate, SOUTH));
            }
            if(isTilePlayable(tileToPlay, coordinate, WEST)){
                playableTilePositions.add(new TilePosition(coordinate, WEST));
            }
        }
        return playableTilePositions;
    }

    private boolean isTilePlayable(Tile tileToPlay, Coordinate coordinate, PointOfCompass pointOfCompass) {
        if (tileToPlay == null){
            return false;
        }

        Tile tileToNorth = getTile(coordinate.getX(), coordinate.getY() - 1);
        if (tileToNorth != null && !tileToNorth.getTileSide(SOUTH).equals(tileToPlay.getTileSide(NORTH.subtract(pointOfCompass)))){
            return false;
        }
        Tile tileToEast = getTile(coordinate.getX() + 1, coordinate.getY());
        if (tileToEast != null && !tileToEast.getTileSide(WEST).equals(tileToPlay.getTileSide(EAST.subtract(pointOfCompass)))){
            return false;
        }
        Tile tileToSouth = getTile(coordinate.getX(), coordinate.getY() + 1);
        if (tileToSouth != null && !tileToSouth.getTileSide(NORTH).equals(tileToPlay.getTileSide(SOUTH.subtract(pointOfCompass)))){
            return false;
        }
        Tile tileToWest = getTile(coordinate.getX() - 1, coordinate.getY());
        if (tileToWest != null && !tileToWest.getTileSide(EAST).equals(tileToPlay.getTileSide(WEST.subtract(pointOfCompass)))){
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

                if (!Boolean.TRUE.equals(carcassonneMoveDTO.getSkip())){
                    Meeple meeple = new Meeple();
                    meeple.setPosition(carcassonneMoveDTO.getCoordinate().getX());
                    meeple.setColor(nextPlayer.getColor());
                    tile.setMeeple(meeple);
                    nextPlayer.setMeeples(nextPlayer.getMeeples() - 1);
                }

                closeRoads(false);
                closeCities(false);
                closeMonasteries(false);

                nextMoveType = TILE;

                //FIXME next player
                nextPlayer = players.get((players.indexOf(nextPlayer) + 1) % players.size());

                legalParts = null;

                TileID tileID = drawIfPossible();

                if (tileID == null){
                    endGame();
                    return;
                }

                tile = TileFactory.createTile(tileID,this);
            }

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeMonasteries(boolean isEnd) {
        Set<Coordinate> positionsToCheck;
        if (isEnd){
            positionsToCheck = tiles.stream().map(Tile::getCoordinate).collect(Collectors.toSet());
        } else {
            int x = tile.getCoordinate().getX();
            int y = tile.getCoordinate().getY();
            positionsToCheck = new HashSet<>();
            positionsToCheck.add(new Coordinate(x - 1, y - 1));
            positionsToCheck.add(new Coordinate(x - 1, y));
            positionsToCheck.add(new Coordinate(x - 1, y + 1));
            positionsToCheck.add(new Coordinate(x, y - 1));
            positionsToCheck.add(new Coordinate(x, y));
            positionsToCheck.add(new Coordinate(x, y + 1));
            positionsToCheck.add(new Coordinate(x + 1, y - 1));
            positionsToCheck.add(new Coordinate(x + 1, y));
            positionsToCheck.add(new Coordinate(x + 1, y + 1));
        }

        Set<Coordinate> positionsWithMonastery = new HashSet<>();
        for (Coordinate position: positionsToCheck){
            Tile t = getTile(position);
            if (t != null && t.getMonasteryPosition() != null && t.getMeeple() != null && t.getMeeple().getPosition() == t.getMonasteryPosition()){
                positionsWithMonastery.add(position);
            }
        }

        for(Coordinate position: positionsWithMonastery){
            int x2 = position.getX();
            int y2 = position.getY();
            int filled = 0;
            if (getTile(x2 - 1, y2 - 1) != null){
                filled ++;
            }
            if (getTile(x2 - 1, y2) != null){
                filled ++;
            }
            if (getTile(x2 - 1, y2 + 1) != null){
                filled ++;
            }
            if (getTile(x2, y2 - 1) != null){
                filled ++;
            }
            if (getTile(x2, y2) != null){
                filled ++;
            }
            if (getTile(x2, y2 + 1) != null){
                filled ++;
            }
            if (getTile(x2 + 1, y2 - 1) != null){
                filled ++;
            }
            if (getTile(x2 + 1, y2) != null){
                filled ++;
            }
            if (getTile(x2 + 1, y2 + 1) != null){
                filled ++;
            }

            if (isEnd || filled == 9){
                Tile t2 = getTile(position);
                Player player = getPlayer(t2.getMeeple().getColor());
                player.setVictoryPoints(player.getVictoryPoints() + filled);
                player.setMeeples(player.getMeeples() + 1);
                t2.setMeeple(null);
            }
        }

    }



    private Player getPlayer(Color color){
        return players.stream().filter(player -> player.getColor().equals(color)).findFirst().orElse(null);
    }

    private void closeRoads(boolean isEnd) {
        Set<Road> checked = new HashSet<>();

        Set<Road> roads = isEnd ? getAllRoadsWithMeeple() : tile.getRoads();

        for(Road road: roads){
            if (checked.add(road)){
                Set<Road> connectedRoads = getConnectedRoads(road);
                if(isEnd || roadIsClosed(connectedRoads)){
                    Set<Tile> tilesWithRelevantMeeple = connectedRoads
                            .stream()
                            .filter(r -> r.getTile().getMeeple() != null && r.getTile().getMeeple().getPosition() == r.getPosition())
                            .map(Road::getTile)
                            .collect(Collectors.toSet());
                    if(!tilesWithRelevantMeeple.isEmpty()){

                        Set<Map.Entry<Color, Long>> colors = tilesWithRelevantMeeple.stream()
                                .map(Tile::getMeeple)
                                .map(Meeple::getColor)
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                .entrySet();
                        Long maxNum = colors.stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
                        Set<Color> maxColors = colors.stream().filter(c-> c.getValue().equals(maxNum)).map(Map.Entry::getKey).collect(Collectors.toSet());

                        for(Color color: maxColors){
                            Player player = getPlayer(color);
                            player.setVictoryPoints(player.getVictoryPoints() + connectedRoads.size());
                        }
                        tilesWithRelevantMeeple.forEach(tile-> {
                            Player player = getPlayer(tile.getMeeple().getColor());
                            player.setMeeples(player.getMeeples() + 1);
                            tile.setMeeple(null);
                        });
                    }
                }
            }
        }
    }

    private Set<Road> getAllRoadsWithMeeple() {
        Set<Road> result = new HashSet<>();
        for (Tile tile : tiles){
            if (tile.getMeeple() != null){
                Object object = tile.getPart(tile.getMeeple().getPosition());
                if (object instanceof Road){
                    result.add((Road) object);
                }
            }
        }
        return result;
    }

    private boolean roadIsClosed(Set<Road> connectedRoads) {
        Set<Tile> tiles = connectedRoads.stream().map(Road::getTile).collect(Collectors.toSet());
        for(Road road: connectedRoads){
            for(PointOfCompass side: road.getSides()){
                Tile tile = getTile(getCoordinate(road.getTile().getCoordinate(), road.getTile().getPointOfCompass().add(side)));
                if (tile == null || !tiles.contains(tile)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean cityIsClosed(Set<City> connectedCities) {
        Set<Tile> tiles = connectedCities.stream().map(City::getTile).collect(Collectors.toSet());
        for(City city: connectedCities){
            for(PointOfCompass side: city.getSides()){
                Tile tile = getTile(getCoordinate(city.getTile().getCoordinate(), city.getTile().getPointOfCompass().add(side)));
                if (tile == null || !tiles.contains(tile)){
                    return false;
                }
            }
        }
        return true;
    }

    private Coordinate getCoordinate(Coordinate coordinate, PointOfCompass pointOfCompass){
        switch (pointOfCompass){
            case NORTH:
                return new Coordinate(coordinate.getX(), coordinate.getY() - 1);
            case EAST:
                return new Coordinate(coordinate.getX() + 1, coordinate.getY());
            case SOUTH:
                return new Coordinate(coordinate.getX(), coordinate.getY() + 1);
            case WEST:
                return new Coordinate(coordinate.getX() - 1, coordinate.getY());
            default:return null;
        }
    }


    private Set<Road> getConnectedRoads(Road road) {
        Set<Road> checked = new HashSet<>();
        checked.add(road);
        return getConnectedRoads(road, checked);
    }

    private Set<City> getConnectedCities(City city) {
        Set<City> checked = new HashSet<>();
        checked.add(city);
        return getConnectedCities(city, checked);
    }

    private Set<Field> getConnectedFields(Field field) {
        Set<Field> checked = new HashSet<>();
        checked.add(field);
        return getConnectedFields(field, checked);
    }

    private Set<Field> getConnectedFields(Field field, Set<Field> checked) {
        for(Field neighborField: getNeighborFields(field)){
            if (checked.add(neighborField)){
                checked.addAll(getConnectedFields(neighborField, checked));
            }
        }
        return checked;
    }

    private Set<City> getConnectedCities(City city, Set<City> checked) {
        for(City neighborCity: getNeighborCities(city)){
            if (checked.add(neighborCity)){
                checked.addAll(getConnectedCities(neighborCity, checked));
            }
        }
        return checked;
    }

    private Set<Road> getConnectedRoads(Road road, Set<Road> checked) {
        for(Road neighborRoad: getNeighborRoads(road)){
            if (checked.add(neighborRoad)){
                checked.addAll(getConnectedRoads(neighborRoad, checked));
            }
        }
        return checked;
    }

    private void closeCities(boolean isEnd) {
        Set<City> checked = new HashSet<>();

        Set<City> cities = isEnd ? getAllCitiesWithMeeple() : tile.getCities();

        for(City city: cities){
            if (checked.add(city)){

                Set<City> connectedCities = getConnectedCities(city);

                if(isEnd || cityIsClosed(connectedCities)){
                    cities.forEach(c-> c.setClosed(true));

                    Set<Tile> tilesWithRelevantMeeple = connectedCities
                            .stream()
                            .filter(t -> t.getTile().getMeeple() != null && t.getTile().getMeeple().getPosition() == t.getPosition())
                            .map(City::getTile)
                            .collect(Collectors.toSet());
                    if(!tilesWithRelevantMeeple.isEmpty()){

                        Set<Map.Entry<Color, Long>> colors = tilesWithRelevantMeeple.stream()
                                .map(Tile::getMeeple)
                                .map(Meeple::getColor)
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                .entrySet();
                        Long maxNum = colors.stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
                        Set<Color> maxColors = colors.stream().filter(c-> c.getValue().equals(maxNum)).map(Map.Entry::getKey).collect(Collectors.toSet());

                        for(Color color: maxColors){
                            Player player = getPlayer(color);
                            player.setVictoryPoints(player.getVictoryPoints() + (connectedCities.size() + connectedCities.stream().filter(City::isHasShield).collect(Collectors.toSet()).size()) * (isEnd ? 1 : 2));
                        }
                        tilesWithRelevantMeeple.forEach(tile-> {
                            Player player = getPlayer(tile.getMeeple().getColor());
                            player.setMeeples(player.getMeeples() + 1);
                            tile.setMeeple(null);
                        });
                    }
                }
            }
        }
    }

    private Set<City> getAllCitiesWithMeeple() {
        Set<City> result = new HashSet<>();
        for (Tile tile : tiles){
            if (tile.getMeeple() != null){
                Object object = tile.getPart(tile.getMeeple().getPosition());
                if (object instanceof City){
                    result.add((City) object);
                }
            }
        }
        return result;
    }

    private Set<Field> getAllFieldsWithMeeple() {
        Set<Field> result = new HashSet<>();
        for (Tile tile : tiles){
            if (tile.getMeeple() != null){
                Object object = tile.getPart(tile.getMeeple().getPosition());
                if (object instanceof Field){
                    result.add((Field) object);
                }
            }
        }
        return result;
    }


    private void endGame() {
        closeRoads(true);
        closeCities(true);
        closeMonasteries(true);
        closeFields();

        winners = players.stream().filter(player -> player.getVictoryPoints().equals(players.stream().map(Player::getVictoryPoints).max(Integer::compareTo).orElse(null))).collect(Collectors.toSet());

        gameState = GameState.ENDED;
    }

    private void closeFields() {

        Set<Field> fields = getAllFieldsWithMeeple();

        Set<Field> checked = new HashSet<>();
        for(Field field: fields){
            if (checked.add(field)){

                Set<Field> connectedFields = getConnectedFields(field);

                Set<Tile> tilesWithRelevantMeeple = connectedFields
                        .stream()
                        .filter(t -> t.getTile().getMeeple() != null && t.getTile().getMeeple().getPosition() == t.getPosition())
                        .map(Field::getTile)
                        .collect(Collectors.toSet());

                Set<Map.Entry<Color, Long>> colors = tilesWithRelevantMeeple.stream()
                        .map(Tile::getMeeple)
                        .map(Meeple::getColor)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet();
                Long maxNum = colors.stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
                Set<Color> maxColors = colors.stream().filter(c-> c.getValue().equals(maxNum)).map(Map.Entry::getKey).collect(Collectors.toSet());

                for(Color color: maxColors){
                    Player player = getPlayer(color);
                    player.setVictoryPoints(player.getVictoryPoints() + 3 * getNumberOfClosedCitiesOnField(connectedFields));
                }
                tilesWithRelevantMeeple.forEach(tile-> {
                    Player player = getPlayer(tile.getMeeple().getColor());
                    player.setMeeples(player.getMeeples() + 1);
                    tile.setMeeple(null);
                });
            }
        }

    }

    private int getNumberOfClosedCitiesOnField(Set<Field> fields){
        int result = 0;
        Set<City> checked = new HashSet<>();
        for(Field field: fields){
            for(City city: field.getCities()){
                if (!checked.contains(city)){
                    checked.addAll(getConnectedCities(city));
                    if (city.isClosed()){
                        result ++;
                    }
                }
            }
        }
        return result;
    }

    private TileID drawIfPossible(){
        List<TileID> drawn = new ArrayList<>();
        TileID tileID = null;

        while (deck.isNotEmpty()){
            tileID = deck.draw();

            if (drawn.contains(tileID)){
                drawn.add(tileID);
                tileID = null;
                continue;
            }

            Tile tileToPlay = TileFactory.createTile(tileID, this);
            playableTilePositions = getPlayableTilePositions(tileToPlay);
            if (!playableTilePositions.isEmpty()){
                break;
            }

            drawn.add(tileID);
            tileID = null;
        }

        TileID finalTileID = tileID;
        drawn.stream().filter(t-> !t.equals(finalTileID)).forEach(t-> deck.put(t));

        return finalTileID;
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

    private Tile getTile(Coordinate coordinate){
        return getTile(coordinate.getX(), coordinate.getY());
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
