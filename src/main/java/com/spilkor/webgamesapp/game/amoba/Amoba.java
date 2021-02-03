package com.spilkor.webgamesapp.game.amoba;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.Coordinate;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;

//    2|2 2|1 2|0
//    1|2 1|1 1|0
//    0|2 0|1 0|0

//    null: empty
//    true: X
//    false: O
//    X starts

public class Amoba extends Game {

    private UserDTO nextPlayer = null;
    private UserDTO winner = null;
    private UserDTO startingPlayer = null;
    private OwnerAs ownerAs;
    private Boolean nextSign = null;
    private AmobaSize amobaSize;
    private Boolean[][] table;
    private Coordinate lastPosition = null;

    public Amoba(UserDTO owner, GameType gameType){
        super(owner, gameType);

        ownerAs = OwnerAs.Random;
        amobaSize = AmobaSize.three;
    }

    @Override
    public void surrender(UserDTO userDTO) {
//        TODO
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        try {
            AmobaLobbyDTO amobaLobbyDTO = Mapper.readValue(lobbyJSON, AmobaLobbyDTO.class);

            if (amobaLobbyDTO.getOwnerAs() == null && amobaLobbyDTO.getAmobaSize() == null){
                return false;
            }

            if (amobaLobbyDTO.getOwnerAs() != null){
                ownerAs = amobaLobbyDTO.getOwnerAs();
            }

            if (amobaLobbyDTO.getAmobaSize() != null){
                amobaSize = amobaLobbyDTO.getAmobaSize();
            }

            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStartable() {
        return players.size() == 2;
    }

    @Override
    public void start() {
        switch (amobaSize){
            case three:
                table = new Boolean[3][3];
                nextSign = true;
                switch (ownerAs){
                    case X:
                        startingPlayer = owner;
                        break;
                    case O:
                        startingPlayer = getSecondPlayer();
                        break;
                    case Random:
                        startingPlayer = MathUtil.coinToss() ? owner : getSecondPlayer();
                        break;
                }
                break;
            case twoHundred:
                table = new Boolean[200][200];
                table[100][100] = true;

                lastPosition = new Coordinate(100,100);
                nextSign = false;

                switch (ownerAs){
                    case X:
                        startingPlayer = getSecondPlayer();
                        break;
                    case O:
                        startingPlayer = owner;
                        break;
                    case Random:
                        startingPlayer = MathUtil.coinToss() ? owner : getSecondPlayer();
                        break;
                }
                break;
        }
        nextPlayer = startingPlayer;
    }

    @Override
    public void restart() {
        table = null;
        winner = null;
        nextPlayer = null;
        nextSign = null;
        lastPosition = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        AmobaGameDTO amobaGameDTO = new AmobaGameDTO();
        amobaGameDTO.setNextPlayer(nextPlayer);
        amobaGameDTO.setTable(table);
        amobaGameDTO.setWinner(winner);
        amobaGameDTO.setOwnerAs(ownerAs);
        amobaGameDTO.setAmobaSize(amobaSize);
        amobaGameDTO.setNextSign(nextSign);
        amobaGameDTO.setLastPosition(lastPosition);
        try {
            return Mapper.writeValueAsString(amobaGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
        try {
            AmobaMoveDTO amobaMoveDTO = Mapper.readValue(moveJSON, AmobaMoveDTO.class);

            if (!nextPlayer.equals(userDTO)){
                return false;
            }

            Coordinate position = amobaMoveDTO.getPosition();
            if (position == null){
                return false;
            }

            switch (amobaSize){
                case three:
                    if (!MathUtil.inRange(0, position.getX(), 8) || !MathUtil.inRange(0, position.getY(), 8) ){
                        return false;
                    }
                    return table[position.getX()][position.getY()] == null;
                case twoHundred:
                    if (!MathUtil.inRange(0, position.getX(), 199) || !MathUtil.inRange(0, position.getY(), 199) ){
                        return false;
                    }
                    return table[position.getX()][position.getY()] == null;
                default:
                    return false;
            }
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            AmobaMoveDTO amobaMoveDTO = Mapper.readValue(moveJSON, AmobaMoveDTO.class);

            Coordinate position = amobaMoveDTO.getPosition();
            table[position.getX()][position.getY()] = nextSign;
            nextSign = !nextSign;
            lastPosition = new Coordinate(position.getX(), position.getY());

            boolean hasLine = hasLine(lastPosition, amobaSize.getLineLength());
            boolean tableIsFull = tableIsFull();
            boolean gameEnded = hasLine || tableIsFull;

            if (gameEnded){
                gameState = GameState.ENDED;
                winner = hasLine ? nextPlayer.equals(owner) ? owner : getSecondPlayer() : null;
                nextPlayer = null;
            } else {
                nextPlayer = nextPlayer.equals(owner) ? getSecondPlayer() : owner;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private boolean tableIsFull() {
        for (int x = 0; x < table.length; x++){
            for (int y = 0; y < table[0].length; y++){
                if (table[x][y] == null){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasLine(Coordinate lastPosition, int length) {
        int north = getNumberOfSquaresInDirectionWithSameValue(lastPosition, 1, 0);
        int northEast = getNumberOfSquaresInDirectionWithSameValue(lastPosition, 1, -1);
        int east = getNumberOfSquaresInDirectionWithSameValue(lastPosition, 0, -1);
        int southEast = getNumberOfSquaresInDirectionWithSameValue(lastPosition, -1, -1);
        int south = getNumberOfSquaresInDirectionWithSameValue(lastPosition, -1, 0);
        int southWest = getNumberOfSquaresInDirectionWithSameValue(lastPosition, -1, 1);
        int west = getNumberOfSquaresInDirectionWithSameValue(lastPosition, 0, 1);
        int northWest = getNumberOfSquaresInDirectionWithSameValue(lastPosition, 1, 1);

        return north + south + 1 >= length
                        || northEast + southWest + 1 >= length
                        || east + west + 1 >= length
                        || southEast + northWest + 1 >= length;
    }

    private int getNumberOfSquaresInDirectionWithSameValue(Coordinate position, int x, int y) {
        Boolean value = table[lastPosition.getX()][lastPosition.getY()];
        if (value == null){
            return 0;
        }

        Coordinate positionToCheck = new Coordinate(position.getX() + x, position.getY() + y);

        if (positionToCheck.getX() < 0 || table.length <= positionToCheck.getX() || positionToCheck.getY() < 0 || table[positionToCheck.getX()].length <= positionToCheck.getY()){
            return 0;
        }

        if (!value.equals(table[positionToCheck.getX()][positionToCheck.getY()])){
            return 0;
        }

        return 1 + getNumberOfSquaresInDirectionWithSameValue(positionToCheck, x, y);
    }

}
