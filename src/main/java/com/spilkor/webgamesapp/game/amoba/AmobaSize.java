package com.spilkor.webgamesapp.game.amoba;

public enum AmobaSize {

    three(3),
    twoHundred(5);

    private final int lineLength;

    AmobaSize(int lineLength) {
        this.lineLength = lineLength;
    }

    public int getLineLength() {
        return lineLength;
    }
}