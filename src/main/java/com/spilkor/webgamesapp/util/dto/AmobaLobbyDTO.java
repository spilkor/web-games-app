package com.spilkor.webgamesapp.util.dto;

import com.spilkor.webgamesapp.util.enums.OwnerAs;

import java.io.Serializable;

public class AmobaLobbyDTO implements Serializable {

    private OwnerAs ownerAs;

    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }
}