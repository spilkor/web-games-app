package com.spilkor.webgamesapp.util.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.util.GroupHandler;

import java.io.Serializable;

public class Amoba extends Game {

    public static class AmobaLobbyDTO implements Serializable {

        private OwnerAs ownerAs;

        public OwnerAs getOwnerAs() {
            return ownerAs;
        }

        public void setOwnerAs(OwnerAs ownerAs) {
            this.ownerAs = ownerAs;
        }
    }

    public enum OwnerAs {
        Random,
        X,
        O
    }

    private OwnerAs ownerAs = OwnerAs.Random;

    public Amoba(UserDTO owner){
        super(owner);
    }

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }


    public static Amoba newInstance(UserDTO owner){
        Amoba amoba = new Amoba(owner);
        return amoba;
    }

    @Override
    public boolean getStartable(UserDTO user) {
        return owner.equals(user) && players.size() == 2;
    }

    @Override
    public boolean updateLobby(Group group, String lobbyDataJSON) {
        try {
            AmobaLobbyDTO amobaLobbyDTO = mapper.readValue(lobbyDataJSON, AmobaLobbyDTO.class);

            if (amobaLobbyDTO.getOwnerAs() == null){
                return false;
            }

            setOwnerAs(amobaLobbyDTO.getOwnerAs());

            GroupHandler.updateGroup(group);

            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getGameJSON(UserDTO user) {
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


}
