package com.spilkor.webgamesapp.game.snapszer.enums;

public enum TurnValue {

    BASIC("BASIC", 1),
    KONTRA("KONTRA",2),
    RE_KONTRA("RE_KONTRA",4),
    SZUB_KONTRA("SZUB_KONTRA",8),
    MORD_KONTRA("MORD_KONTRA",16),
    HIRSCH_KONTRA("HIRSCH_KONTRA",32),
    FEDAK_SARI("FEDAK_SARI",64),
    KEREKES_BICIKLI("KEREKES_BICIKLI",128);

    public final int value;
    public final String stringValue;

    TurnValue(String stringValue, int value) {
        this.stringValue = stringValue;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public TurnValue getNextTurnValue() {
        switch (this){
            case BASIC:
                return TurnValue.KONTRA;
            case KONTRA:
                return TurnValue.RE_KONTRA;
            case RE_KONTRA:
                return TurnValue.SZUB_KONTRA;
            case SZUB_KONTRA:
                return TurnValue.MORD_KONTRA;
            case MORD_KONTRA:
                return TurnValue.HIRSCH_KONTRA;
            case HIRSCH_KONTRA:
                return TurnValue.FEDAK_SARI;
            case FEDAK_SARI:
                return TurnValue.KEREKES_BICIKLI;
        }
        return null;
    }
}