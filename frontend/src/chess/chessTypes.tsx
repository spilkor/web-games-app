
import {Coordinate, User} from "../util/types";
import {number} from "prop-types";


export type SquireProps = {
    position: Coordinate
}

export enum OwnerAs {
    Random = "Random",
    WHITE = "WHITE",
    BLACK = "BLACK"
}

export type ChessLobbyDTO = {
    ownerAs: OwnerAs
}

export type ChessGameDTO = {
    // nextPlayer: User | null,
    // ownerAs: OwnerAs,
    // startingPlayer: User,
    // table: ChessPiece[][]


    ownerColor: Color
    table: Piece[][]
    nextPlayer: User
    winner: User
    draw: boolean
    ownerAs: OwnerAs
    surrendered: User
}

export type ChessMoveDTO = {
    source: Position,
    target: Position
    promoteType: PieceType
}

export type Piece = {
    pieceType: PieceType
    color: Color
}

export enum PieceType {
    KING = "KING",
    QUEEN = "QUEEN",
    ROOK = "ROOK",
    BISHOP = "BISHOP",
    KNIGHT = "KNIGHT",
    PAWN = "PAWN"
}

export enum Color {
    WHITE = "WHITE",
    BLACK = "BLACK"
}

export type Position = {
    row: number
    column: number
}
