import {Position} from "../components/Game";
import {User} from "../util/types";


export type SquireProps = {
    position: Position
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
    nextPlayer: User | null,
    ownerAs: OwnerAs,
    startingPlayer: User,
    table: ChessPiece[][]
}

export type ChessMoveDTO = {
    fromPosition: Position,
    toPosition: Position
}

export type ChessPiece = {
    type: ChessPieceType
    color: Color
}

export enum ChessPieceType {
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
