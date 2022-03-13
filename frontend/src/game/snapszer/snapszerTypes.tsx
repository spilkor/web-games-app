import {User} from "../../util/types";
import React from "react";


export type Player = {
    user: User
    cards: Card[]
    withCaller: boolean | null
}

export type SnapszerGameDTO = {
    players: Player[]
    nextPlayer: Player | null
    round: Round | null
    rounds: Round[] | null
    scoreBoard: (number | null)[]
}

export type Round = {
    caller: Player
    roundState: RoundState
    csapCards: Card[] | null
    csapCard: Card | null
    calledCard: Card | null
    adu: Color | null
    snapszer: boolean
    turnValue: TurnValue
    turn: Turn | null
    turns: Turn[] | null
    scoreBoard: (number | null)[] | null
    firstLicitTurn: boolean
}

export type Turn = {
    cards: Card[] | null
    caller: Player | null
    twenty: boolean | null
    forty: boolean | null
    ended: boolean
    strongestPlayer: Player | null
}

export type SnapszerMoveDTO = {
    csapIndex: number | null
    csapFigure: Figure | null
    licit: Licit | null
    card: Card | null
    actionType: ActionType | null
}

export enum ActionType {
    TWENTY = "TWENTY",
    FORTY = "FORTY",
    PLAY_CARD = "PLAY_CARD",
    STOP = "STOP",
    KOPP = "KOPP"
}

export enum RoundState {
    CALL_CARD = "CALL_CARD",
    CALL_FIGURE = "CALL_FIGURE",
    LICIT = "LICIT",
    TURNS = "TURNS"
}

export type Card = {
    color: Color;
    figure: Figure;
}

export enum Licit {
    CHECK = "CHECK",
    SNAPSZER = "SNAPSZER",
    THROW_IN = "THROW_IN",
    CONTRA = "CONTRA",
    CONTRA_SNAPSZER = "CONTRA_SNAPSZER",
    THREE_NINE = "THREE_NINE"
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
    UNKNOWN = "UNKNOWN",
    TWENTY = "TWENTY",
    FORTY = "FORTY"
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