import {PointOfCompass, Coordinate, User} from "../util/types";
import React from "react";
import {AmobaSize, OwnerAs} from "../amoba/amobaTypes";

export enum Color {
    RED = "RED",
    GREEN = "GREEN",
    BLUE = "BLUE",
    YELLOW = "YELLOW"
}

export type TileDTO = {
    id: TileID,
    legalParts: number[] | null
    meeple?: MeepleType
    pointOfCompass: PointOfCompass
    coordinate: Coordinate
}

export type MeepleType = {
    color: Color,
    position: number
}

export type Player = {
    user: User
    color: Color
}


export type CarcassonneGameDTO = {

    players: Player[]
    nextPlayer: User | null
    winner: User

    tiles: TileDTO[]
    tile?: TileDTO


    lastTileCoordinate: Coordinate
    nextMoveType: MoveType
}

export enum MoveType {

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
    legalParts: number[] | null,
    partIndex: number
}

export type MeepleProps = {
    position: Coordinate
    color: Color
    pointOfCompass: PointOfCompass
}

export const pointOfCompassMap : {[x: string]:number}= {
    "NORTH": 0,
    "EAST": 90,
    "SOUTH": 180,
    "WEST": 270
};