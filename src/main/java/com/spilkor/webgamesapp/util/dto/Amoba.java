package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.util.WebMathUtil;
import com.spilkor.webgamesapp.util.enums.GameState;
import com.spilkor.webgamesapp.util.enums.OwnerAs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


//        0 1 2            null: empty,  true: X,  false: O
//        3 4 5
//        6 7 8            X starts


public class Amoba extends Game {

    private UserDTO nextPlayer = null;
    private UserDTO winner = null;
    private UserDTO startingPlayer = null;
    private Boolean[] table = null;
    private OwnerAs ownerAs = OwnerAs.Random;

    private Amoba(Group group){
        super(group);
    }

    public static Amoba newInstance(Group group){
        Amoba amoba = new Amoba(group);
        amoba.ownerAs = OwnerAs.Random;
        return amoba;
    }

    @Override
    public boolean isStartable() {
        return group.getPlayers().size() == 2;
    }

    @Override
    public boolean updateLobby(String lobbyDataJSON) {
        try {
            AmobaLobbyDTO amobaLobbyDTO = mapper.readValue(lobbyDataJSON, AmobaLobbyDTO.class);

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
    public void startGame() {

        table = new Boolean[] {
                null,null,null,
                null,null,null,
                null,null,null
        };

        switch (ownerAs){
            case X:
                startingPlayer = group.getOwner();
                break;
            case O:
                startingPlayer = group.getSecondPlayer();
                break;
            case Random:
                if (WebMathUtil.coinToss()){
                    startingPlayer = group.getOwner();
                } else {
                    startingPlayer = group.getSecondPlayer();
                }
                break;
        }

        nextPlayer = startingPlayer;
    }


    @Override
    public String getGameJSON(UserDTO user) {
        AmobaGameDTO amobaGameDTO = new AmobaGameDTO();
        amobaGameDTO.setNextPlayer(nextPlayer);
        amobaGameDTO.setTable(table);
        try {
            return mapper.writeValueAsString(amobaGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getEndJSON(UserDTO user) {
        AmobaEndDTO amobaEndDTO = new AmobaEndDTO();
        amobaEndDTO.setWinner(winner);
        try {
            return mapper.writeValueAsString(amobaEndDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getLobbyJSON(UserDTO user) {
        AmobaLobbyDTO amobaLobbyDTO = new AmobaLobbyDTO();
        amobaLobbyDTO.setOwnerAs(ownerAs);
        try {
            return mapper.writeValueAsString(amobaLobbyDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static class AmobaGameDTO implements Serializable {

        private UserDTO nextPlayer;

        private Boolean[] table;

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
    }

    public static class AmobaEndDTO implements Serializable {
        private UserDTO winner;

        public UserDTO getWinner() {
            return winner;
        }

        public void setWinner(UserDTO winner) {
            this.winner = winner;
        }
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

            table[amobaMoveDTO.getIndex()] = startingPlayer.equals(userDTO);

            boolean hasRow = hasRow();
            boolean tableIsFull = tableIsFull();
            boolean gameEnded = hasRow || tableIsFull;

            if (gameEnded){
                group.setGameState(GameState.GAME_END);
                winner = hasRow ? nextPlayer.equals(group.getOwner()) ? group.getOwner() : group.getSecondPlayer() : null;
                nextPlayer = null;
            } else {
                nextPlayer = nextPlayer.equals(group.getOwner()) ? group.getSecondPlayer() : group.getOwner();
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


}
