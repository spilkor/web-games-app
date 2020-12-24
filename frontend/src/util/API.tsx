import { User} from "./types";
import {log} from "../index";
import {GameData } from "../App";
import {GameType} from "../components/Game";

function returnResponse (response: any){
    return response;
}

async function translateJSON (response: any){
    return response.json();
}

export default class API {

    public static async getUserToken(): Promise<string>{
        log("getUserToken");
        let message = await fetch("/api/userToken", {
            method: 'GET',
            credentials: 'include'
        }).then(translateJSON)
            .then(returnResponse);
        return JSON.parse(message.data) as string;
    }

    public static async getUser(): Promise<User>{
        let res = await fetch('/api/user', {
            method: 'GET',
            credentials: 'include'
        }).then(translateJSON)
            .then(returnResponse)
            .catch((error=>{return null}));
        return res as User;
    }


    public static async getGameData(): Promise<GameData>{
        let res = await fetch('/api/game', {
            method: 'GET',
            credentials: 'include'
        }).then(translateJSON)
            .then(returnResponse)
            .catch((error=>{return null}));
        return res as GameData;
    }

    public static async getInvites(): Promise<User[]>{
        let res = await fetch('/api/my-invites', {
            method: 'GET',
            credentials: 'include'
        }).then(translateJSON)
            .then(returnResponse)
            .catch((error=>{return null}));
        return res as User[];
    }

    public static async logout() {
        await fetch('/api/logout', {
            method: 'GET',
            credentials: 'include'
        });
    }

    public static async login(userName: string, password: string): Promise<Error | undefined>{
        return await fetch('/api/login', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                    userName: userName,
                    password: password
                }
            )
        }).then((response) => {
            if (response.ok) {
                return;
            } else {
                if (response.status === 404) {
                    return Error("Invalid UserName / Password");
                } else {
                    return Error("Unknown Error");
                }
            }
        }).then(returnResponse);
    }

    public static async removeFriend(id: string) {
        await fetch('/api/remove-friend', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: id
        });
    }

    public static async requestFriend(userName: string): Promise<Error | undefined> {
        return await fetch('/api/friend-request', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: userName
        }).then(function (response) {
            if (response.ok) {
                return;
            } else {
                if (response.status === 409) {
                    return Error("You cannot add yourself as friend");
                } else if (response.status === 404) {
                    return Error("There is no user with this name");
                } else if (response.status === 302) {
                    return Error("You are already friends");
                } else if (response.status === 403) {
                    return Error("You already sent a request to this user");
                } else {
                    return Error("Unknown Error");
                }
            }
        }).then(returnResponse);
    }

    public static async createNewAccount(userName: string, password: string) :Promise<Error | undefined> {
        return await fetch('/api/create-account', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                    userName: userName,
                    password: password
                }
            )
        }).then(function (response) {
            if (response.ok) {
                return;
            } else {
                if (response.status === 409) {
                    return Error("UserName already taken");
                } else if (response.status === 406) {
                    return Error("UserName and Password must be between 3 and 10 letters");
                } else {
                    return Error("Unknown Error");
                }
            }
        }).then(returnResponse);
    }


    public static async sendChatMessage(text: string) {
        log("api.sendChatMessage: " + text);
        return await fetch('/api/chat-message', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: text
        }).then(res => {
            return res.ok
        });
    }

    public static async createGame(gameType: GameType) {
        await fetch('/api/create-game/' + gameType, {
            method: 'GET',
            credentials: 'include'
        });
    }

    public static async sendLobbyData(lobbyJSON: string) {
        log("api.sendLobbyData: " + lobbyJSON);
        return await fetch('/api/lobby', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: lobbyJSON
        }).then(res => {
            return res.ok
        });
    }

    public static async invite(userId: string) {
        await fetch('/api/invite/' + userId, {
            method: 'GET',
            credentials: 'include'
        });
    }

    public static async accept(ownerId: string) {
        await fetch('/api/accept-invite/' + ownerId, {
            method: 'GET',
            credentials: 'include'
        });
    }

    public static async startGame() {
        console.log("start-game");
        await fetch('/api/start-game', {
            method: 'GET',
            credentials: 'include'
        });
    }

    public static async restartGame() {
        console.log("restart-game");
        await fetch('/api/restart-game', {
            method: 'GET',
            credentials: 'include'
        });
    }

    public static async move(moveJSON: string) {
        await fetch('/api/move', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: moveJSON
        });
    }
}




