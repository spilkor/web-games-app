package com.spilkor.webgamesapp.game.amoba;

import java.io.Serializable;

public class AmobaLobbyDTO implements Serializable {

    private Amoba.OwnerAs ownerAs;

    public Amoba.OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(Amoba.OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }
}