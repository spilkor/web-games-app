package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.util.WebMathUtil;
import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.GameType;
import com.spilkor.webgamesapp.util.enums.OwnerAs;

import java.io.Serializable;


public class Amoba extends Game {

//    [0 1 2
//    3 4 5
//    6 7 8]
//    null: empty,  true: X,  false: O
//    X starts

    private UserDTO nextPlayer = null;
    private UserDTO winner = null;
    private UserDTO startingPlayer = null;
    private Boolean[] table;
    private OwnerAs ownerAs;
    private Boolean nextSign = null;

    public Amoba(UserDTO owner, GameType gameType){
        super(owner, gameType);

        ownerAs = OwnerAs.Random;
        table = new Boolean[] {
                null,null,null,
                null,null,null,
                null,null,null
        };
    }


    @Override
    public boolean updateLobby(String lobbyJSON) {
        try {
            AmobaLobbyDTO amobaLobbyDTO = mapper.readValue(lobbyJSON, AmobaLobbyDTO.class);

            if (amobaLobbyDTO.getOwnerAs() == null){
                return false;
            }

            ownerAs = amobaLobbyDTO.getOwnerAs();

            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStartable() {
        return players.size() == 2;// && ownerAs != null;
    }

    @Override
    public void start() {
        switch (ownerAs){
            case X:
                startingPlayer = owner;
                break;
            case O:
                startingPlayer = getSecondPlayer();
                break;
            case Random:
                if (WebMathUtil.coinToss()){
                    startingPlayer = owner;
                } else {
                    startingPlayer = getSecondPlayer();
                }
        }
        nextPlayer = startingPlayer;
        nextSign = true;
    }

    @Override
    public void restart() {
        table = new Boolean[] {
                null,null,null,
                null,null,null,
                null,null,null
        };
        winner = null;
        nextPlayer = null;
        nextSign = true;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        AmobaGameDTO amobaGameDTO = new AmobaGameDTO();
        amobaGameDTO.setNextPlayer(nextPlayer);
        amobaGameDTO.setTable(table);
        amobaGameDTO.setWinner(winner);
        amobaGameDTO.setOwnerAs(ownerAs);
        amobaGameDTO.setNextSign(nextSign);
        try {
            return mapper.writeValueAsString(amobaGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
        try {
            AmobaMoveDTO amobaMoveDTO = mapper.readValue(moveJSON, AmobaMoveDTO.class);
            if (nextPlayer.equals(userDTO)){
                if (amobaMoveDTO.getIndex()>=0 && amobaMoveDTO.getIndex()<=8){
                    return table[amobaMoveDTO.getIndex()] == null;
                }
            }
        } catch (JsonProcessingException e) {
            return false;
        }
        return false;
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            AmobaMoveDTO amobaMoveDTO = mapper.readValue(moveJSON, AmobaMoveDTO.class);

            table[amobaMoveDTO.getIndex()] = nextSign;
            nextSign = !nextSign;

            boolean hasRow = hasRow();
            boolean tableIsFull = tableIsFull();
            boolean gameEnded = hasRow || tableIsFull;

            if (gameEnded){
                gameState = GameState.ENDED;
                winner = hasRow ? nextPlayer.equals(owner) ? owner : getSecondPlayer() : null;
                nextPlayer = null;
            } else {
                nextPlayer = nextPlayer.equals(owner) ? getSecondPlayer() : owner;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private boolean tableIsFull() {
        return
                table[0] != null &&
                table[1] != null &&
                table[2] != null &&
                table[3] != null &&
                table[4] != null &&
                table[5] != null &&
                table[6] != null &&
                table[7] != null &&
                table[8] != null;
    }

    private boolean hasRow() {
        int[][] rows = new int[][]{
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6},
        };
        for (int[] row: rows){
            if (table[row[0]]!=null && table[row[0]]==table[row[1]] && table[row[0]]==table[row[2]]){
                return true;
            }
        }
        return false;
    }


    public static class AmobaGameDTO implements Serializable {

        private UserDTO nextPlayer;
        private Boolean[] table;
        private UserDTO winner;
        private OwnerAs ownerAs;
        private Boolean nextSign;


        public UserDTO getNextPlayer() {
            return nextPlayer;
        }

        public void setNextPlayer(UserDTO nextPlayer) {
            this.nextPlayer = nextPlayer;
        }

        public Boolean[] getTable() {
            return table;
        }

        public void setTable(Boolean[] table) {
            this.table = table;
        }

        public UserDTO getWinner() {
            return winner;
        }

        public void setWinner(UserDTO winner) {
            this.winner = winner;
        }

        public OwnerAs getOwnerAs() {
            return ownerAs;
        }

        public void setOwnerAs(OwnerAs ownerAs) {
            this.ownerAs = ownerAs;
        }

        public Boolean getNextSign() {
            return nextSign;
        }

        public void setNextSign(Boolean nextSign) {
            this.nextSign = nextSign;
        }
    }

}
