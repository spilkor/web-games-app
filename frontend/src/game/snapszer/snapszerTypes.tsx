import {User} from "../../util/types";
import React from "react";


export type Player = {
    user: User
    cards: Card[]
    points: number | null
    withCaller: boolean | null
    wonRounds: Card[][] | null
}

export type SnapszerGameDTO = {
    players: Player[]
    caller: Player | null
    nextPlayer: Player | null
    gameStatus: GameStatus
    csapCards: Card[] | null
    csapIndex: number | null
    calledCard: Card | null
    adu: Color | null
    snapszer: boolean | null
    turnValue: TurnValue | null
    round: Card[] | null
    lastRound: Card[] | null
}

export type SnapszerMoveDTO = {
    csapIndex: number | null
    csapFigure: Figure | null
    calledCard: Card | null
    act: Act | null
    card: Card | null
}

export enum GameStatus {
    CALL_CARD = "CALL_CARD",
    CALL_FIGURE = "CALL_FIGURE",
    FIRST_ACT = "FIRST_ACT",
    ACT = "ACT",
    PLAY_CARD = "PLAY_CARD"
}

export type Card = {
    color: Color;
    figure: Figure;
}

export enum Act {
    CHECK = "CHECK",
    SNAPSZER = "SNAPSZER",
    THROW_IN = "THROW_IN",
    CONTRA = "CONTRA"
}

export enum Color {
    MAKK = "MAKK",
    TOK = "TOK",
    KOR = "KOR",
    ZOLD = "ZOLD",
    UNKNOWN = "UNKNOWN"
}

export enum Figure {
    ASZ = "ASZ",
    TIZ = "TIZ",
    KIRALY = "KIRALY",
    FELSO = "FELSO",
    ALSO = "ALSO",
    KILENC = "KILENC",
    UNKNOWN = "UNKNOWN"
}

export enum TurnValue {
    BASIC = "BASIC",
    KONTRA = "KONTRA",
    RE_KONTRA = "RE_KONTRA",
    SZUB_KONTRA = "SZUB_KONTRA",
    MORD_KONTRA = "MORD_KONTRA",
    HIRSCH_KONTRA = "HIRSCH_KONTRA",
    FEDAK_SARI = "FEDAK_SARI",
    KEREKES_BICIKLI = "KEREKES_BICIKLI"
}