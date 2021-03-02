import {PointOfCompass, Coordinate, User} from "../util/types";
import React from "react";

export enum Color {
    RED = "RED",
    GREEN = "GREEN",
    BLUE = "BLUE",
    YELLOW = "YELLOW"
}

export type TileDTO = {
    id: TileID,
    meeple?: MeepleType
    pointOfCompass: PointOfCompass
    coordinate: Coordinate
    legalParts: number[] | null
    size: number
}

export type MeepleType = {
    color: Color,
    position: number
    victoryPoints?: number
}

export type CarcassonneLobbyDTO = {
    userId: string,
    color: Color
}

export type Player = {
    user: User
    color: Color
    victoryPoints: number | null
    meeples: number | null
    isWinner: boolean
}

export type CarcassonneMoveDTO = {
    skip: boolean
    coordinate: Coordinate,
    pointOfCompass: PointOfCompass
}

export type CarcassonneGameDTO = {
    players: Player[]
    nextPlayer: Player | null
    finalPlayers: Player[]

    tiles: TileDTO[]
    tile?: TileDTO

    surrendered: Player

    lastTileCoordinate: Coordinate
    nextMoveType: MoveType

    legalParts: number[] | null
    playableTilePositions: TilePosition[]

    deckSize: number
}

export type CarcassonneGameSettingsDTO = {
    nextTilePointOfCompass: PointOfCompass
    isRotating: boolean
    offset_X: number
    offset_Y: number
    size: number
}

export enum MoveType {

    TILE = "TILE",
    MEEPLE = "MEEPLE"

}



export enum TileID {
    TILE_0 = "TILE_0",
    TILE_1 = "TILE_1",
    TILE_2 = "TILE_2",
    TILE_3 = "TILE_3",
    TILE_4 = "TILE_4",
    TILE_5 = "TILE_5",
    TILE_6 = "TILE_6",
    TILE_7 = "TILE_7",
    TILE_8 = "TILE_8",
    TILE_9 = "TILE_9",
    TILE_10 = "TILE_10",
    TILE_11 = "TILE_11",
    TILE_12 = "TILE_12",
    TILE_13 = "TILE_13",
    TILE_14 = "TILE_14",
    TILE_15 = "TILE_15",
    TILE_16 = "TILE_16",
    TILE_17 = "TILE_17",
    TILE_18 = "TILE_18",
    TILE_19 = "TILE_19",
    TILE_20 = "TILE_20",
    TILE_21 = "TILE_21",
    TILE_22 = "TILE_22",
    TILE_23 = "TILE_23"
}

export type ShieldProps= {
    position: Coordinate
}

export type MonasteryProps = {
    partIndex: number
    legalParts: number[] | null
}

export type MeepleProps = {
    position: Coordinate
    color: Color
    pointOfCompass: PointOfCompass
    victoryPoints?: number
}

export const pointOfCompassMap : {[x: string]:number}= {
    "NORTH": 0,
    "EAST": 90,
    "SOUTH": 180,
    "WEST": 270
};

export type TilePosition = {
    coordinate: Coordinate,
    pointOfCompass: PointOfCompass
}