package com.spilkor.webgamesapp.util.enums;

public enum GameType {

    AMOBA("AMOBA"),
    CHESS("CHESS")
    ;

    private String value;

    GameType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
