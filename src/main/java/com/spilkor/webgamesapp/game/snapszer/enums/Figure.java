package com.spilkor.webgamesapp.game.snapszer.enums;

public enum Figure {

    ASZ,
    TIZ,
    KIRALY,
    FELSO,
    ALSO,
    KILENC,
    UNKNOWN;

    public boolean strongerThan(Figure figure) {
        return getValue() > figure.getValue();
    }

    public int getValue(){
        switch (this){
            case ASZ: return 11;
            case TIZ: return 10;
            case KIRALY: return 4;
            case FELSO: return 3;
            case ALSO: return 2;
        }
        return 0;
    }

}