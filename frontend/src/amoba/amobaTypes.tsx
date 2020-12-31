import {Position} from "../components/Game";
import {User} from "../util/types";


export type SquireProps = {
    position: Position,
    size: number
}

export enum OwnerAs {
    Random = "Random",
    X = "X",
    O = "O"
}

export enum AmobaSize{
    three = "three",
    twoHundred = "twoHundred"
}

export const AmobaSizes = {
    three: {
        id: AmobaSize.three,
        name: "3 X 3"
    },
    twoHundred: {
        id: AmobaSize.twoHundred,
        name: "200 X 200"
    }
};

export type AmobaLobbyDTO = {
    ownerAs: OwnerAs,
    amobaSize: AmobaSize
}

export type AmobaGameDTO = {
    nextPlayer: User | null,
    ownerAs: OwnerAs,
    winner: User,
    nextSign: Boolean,
    amobaSize: AmobaSize,
    table: Boolean[][],
    lastPosition: Position
}

export type AmobaMoveDTO = {
    position: Position
}

export type AmobaGameSettings = {
    num_x: number
    middle_x: number
    middle_y: number
}
