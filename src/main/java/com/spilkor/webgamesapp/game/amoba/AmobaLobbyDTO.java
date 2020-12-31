package com.spilkor.webgamesapp.game.amoba;

import java.io.Serializable;

public class AmobaLobbyDTO implements Serializable {

    private OwnerAs ownerAs;
    private AmobaSize amobaSize;


    public OwnerAs getOwnerAs() {
        return ownerAs;
    }

    public void setOwnerAs(OwnerAs ownerAs) {
        this.ownerAs = ownerAs;
    }

    public AmobaSize getAmobaSize() {
        return amobaSize;
    }

    public void setAmobaSize(AmobaSize amobaSize) {
        this.amobaSize = amobaSize;
    }

}