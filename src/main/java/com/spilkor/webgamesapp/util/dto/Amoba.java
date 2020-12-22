package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.util.GroupHandler;
import com.spilkor.webgamesapp.util.Matek;

import com.spilkor.webgamesapp.util.enums.GameState;

import java.io.Serializable;



//        0 1 2            null: empty,  true: X,  false: O
//        3 4 5
//        6 7 8            X starts


public class Amoba extends Game {

    private UserDTO nextPlayer = null;

    private Boolean[][] table = null;

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

        table = new Boolean[][]{
                {true, null, null},
                {null, null, null},
                {null, null, null}
        };

        switch (ownerAs){
            case X:
                nextPlayer = group.getOwner();
                break;
            case O:
                nextPlayer = group.getSecondPlayer();
                break;
            case Random:
                if (Matek.coinToss()){
                    nextPlayer = group.getOwner();
                } else {
                    nextPlayer = group.getSecondPlayer();
                }
                break;
        }
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

    public static class AmobaLobbyDTO implements Serializable {

        private OwnerAs ownerAs;

        public OwnerAs getOwnerAs() {
            return ownerAs;
        }

        public void setOwnerAs(OwnerAs ownerAs) {
            this.ownerAs = ownerAs;
        }
    }

    public static class AmobaGameDTO implements Serializable {

        private UserDTO nextPlayer;

        private Boolean[][] table;

        public UserDTO getNextPlayer() {
            return nextPlayer;
        }

        public void setNextPlayer(UserDTO nextPlayer) {
            this.nextPlayer = nextPlayer;
        }

        public Boolean[][] getTable() {
            return table;
        }

        public void setTable(Boolean[][] table) {
            this.table = table;
        }
    }

    public enum OwnerAs {
        Random,
        X,
        O
    }

}
