import { User } from "./types";
import {LOG_ON} from "../App";

function returnResponse (response: any){
    return response;
}

class API{

    public async getUserToken(): Promise<string>{
        LOG_ON && console.log("getUserToken");
        let message = await fetch("/userToken", {
            method: 'GET',
            credentials: 'include'
        })
            .then( res => res.json())
            .then( res => { return res });
        let token = JSON.parse(message.data) as string;
        return token;
    }

    public async getUser(): Promise<User>{
        let res = await fetch('/user', {
            method: 'GET',
            credentials: 'include'
        }).then( res => res.json()).then( res =>{return res}).catch((error=>{return null}));
        LOG_ON && console.log("api.getUser" + res);
        return res as User;
    }

    public async logout() {
        let res = await fetch('/logout', {
            method: 'GET',
            credentials: 'include'
        });
    }

    public async login(userName: string, password: string): Promise<Error | undefined>{
        const res = await fetch('/login', {
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
        }).then((response)=> {
            if(response.ok){
                return;
            } else {
                if(response.status == 404){
                    return Error("Invalid UserName / Password");
                } else {
                    return Error("Unknown Error");
                }
            }
        }).then(returnResponse);

        return res;
    }

    public async removeFriend(id: string) {
        await fetch('/remove-friend', {
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

    public async requestFriend(userName: string) :Promise<Error | undefined> {

        const res = await fetch('/friend-request', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: userName
        }).then(function(response) {
            if(response.ok){
                return;
            } else {
                if(response.status == 409){
                    return Error("You cannot add yourself as friend");
                } else if(response.status == 404){
                    return Error("There is no user with this name");
                } else if(response.status == 302){
                    return Error("You are already friends");
                } else if(response.status == 403){
                    return Error("You already sent a request to this user");
                } else {
                    return Error("Unknown Error");
                }
            }
        }).then(returnResponse);

        return res;
    }

    public async createNewAccount(userName: string, password: string) :Promise<Error | undefined> {

        const res = await fetch('/create-account', {
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
        }).then(function(response) {
            if(response.ok){
                return;
            } else {
                if(response.status == 409){
                    return Error("UserName already taken");
                } else if(response.status == 406){
                    return Error("UserName and Password must be at least 3 letters");
                } else {
                    return Error("Unknown Error");
                }
            }
        }).then(returnResponse);

        return res;
    }


    public async sendChatMessage(text: string) {
        console.log("api.sendChatMessage:",text);

        const res = await fetch('/chat-message', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: text
        }).then( res =>{return res.ok });

        return res;
    }

}

export const api = new API();



