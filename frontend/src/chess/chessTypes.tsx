
import {Coordinate, User} from "../util/types";

export type SquireProps = {
    position: Coordinate
}

export enum OwnerAs {
    Random = "Random",
    WHITE = "WHITE",
    BLACK = "BLACK"
}

export enum DrawReason {
    STALEMATE = "STALEMATE",
    AGREED_TO_DRAW = "AGREED_TO_DRAW",
    FIFTY_MOVE_RULE = "FIFTY_MOVE_RULE",
    INSUFFICIENT_MATERIAL = "INSUFFICIENT_MATERIAL",
    THREEFOLD_REPETITION = "THREEFOLD_REPETITION",
    FIVEFOLD_REPETITION = "FIVEFOLD_REPETITION"
}

export type ChessLobbyDTO = {
    ownerAs: OwnerAs
}

export type Player = {
    user: User
    color: Color
    isWinner: boolean
}

export type ChessGameDTO = {
    ownerAs: OwnerAs
    players: Player[]
    nextColor: Color
    table: Piece[][]
    winner: Player
    draw: boolean
    surrendered: Player
    drawReason: DrawReason
    waitingForPromotionType: boolean
    drawActive: boolean
}

export type ChessMoveDTO = {
    source: Position
    target: Position
    promoteType: PieceType
    draw: boolean
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
