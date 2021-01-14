import {MessageType} from "./enums";

export enum UserState  {
    ONLINE = "online", OFFLINE = "offline"
}

export type User = {
    id: string,
    name: string,
    userState: UserState
}

export type WSMessage = {
    data: any,
    messageType: MessageType
}

export type ChatMessage = {
    message: string,
    user: User
}

export enum GameState  {
    IN_LOBBY = "IN_LOBBY",
    IN_GAME = "IN_GAME",
    ENDED = "ENDED"
}

export type MenuButtonProps = {
    text: string
    onClick: ()=> any
}

export type GameButtonProps = {
    text: string
    onClick: ()=> any,
    enabled: boolean
}

export type StartGameButtonProps = {
    text: string
    enabled: boolean
}

export type Coordinate = {
    x: number,
    y: number
}


export enum PointOfCompass {
    NORTH = "NORTH",
    EAST = "EAST",
    SOUTH = "SOUTH",
    WEST = "WEST"
}

