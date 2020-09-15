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
