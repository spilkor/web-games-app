import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/app.css';

import {ChatMessage, GameState, User, UserState, WSMessage} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import API from "./util/API";

import {Header} from "./components/Header";
import {Chat} from "./components/Chat";
import {Users, UsersLogo} from "./components/Users";
import {Home} from "./components/Home";
import {IP, log, PORT} from "./index";
import {Game} from "./components/Game";
import {Invite, Invites, InvitesLogo} from "./components/Invites";


export const AppContext = React.createContext<Partial<ContextProps>>({});



export enum ContentMode  {
    HOME = "HOME",
    CHAT = "CHAT",
    GAME = "GAME"
}

export enum GameType  {
    AMOBA = "AMOBA",
    CHESS = "CHESS"
}

export type GameData = {
    startable: Boolean
    gameType: GameType
    gameState: GameState
    players: User[]
    owner: User

    lobbyJSON: string
    gameJSON: string
    endJSON: string
}

export type ContextProps = {
    user: User | null,

    gameData: GameData | null | undefined

    reconnect: () => void

    chatMessages: ChatMessage[]
    friends: User[]
    setContentMode: (contentMode: ContentMode) => void
    gameTypes: GameType[],

    usersOpen: boolean
    setUsersOpen: (usersOpen: boolean) => any

    invites: Invite[]
    setInvitesOpen: (invitesOpen: boolean) => any
    invitesOpen: boolean
};



export function App () {

    const [user, setUser] = useState<User | null | undefined>(undefined);
    const [webSocket, setWebsocket] = useState<WebSocket | null>(null);

    const [gameData, setGameData] = useState<GameData | null>(null);

    const [friends, setFriends] = useState<User[]>([]);


    const [contentMode, setContentMode] = useState<ContentMode>(ContentMode.HOME);
    const [chatMessages, _setChatMessages] = useState<ChatMessage[]>([]);
    const chatMessagesRef = React.useRef(chatMessages);
    const setChatMessages = (chatMessages: ChatMessage[]) => {
        chatMessagesRef.current = chatMessages;
        _setChatMessages(chatMessages);
    };

    const [gameTypes, setGameTypes] = useState<GameType[]>([GameType.AMOBA, GameType.CHESS]); // TODO fetch + cache


    const [usersOpen, setUsersOpen2] = useState<boolean>(false);

    function setUsersOpen(usersOpen: boolean){
        setUsersOpen2(usersOpen);
    }

    const [invitesOpen, setInvitesOpen2] = useState<boolean>(false);
    const [invites, setInvites] = useState<Invite[]>();
    function setInvitesOpen(invitesOpen: boolean){
        setInvitesOpen2(invitesOpen);
    }

    useEffect(() => {
        setInitialState();

        if(user === undefined){
            fetchUser(); //will be null, not undefined if guest
            return;
        }

        if (user === null){
            return;
        }

        createNewWebSocket();

        fetchInvites();

        fetchGameData();

    }, [user]);

    if(user === undefined){
        return null;
    } else {
        return (
            <AppContext.Provider
                value={{
                    user,
                    chatMessages,
                    reconnect,
                    friends,
                    gameTypes,
                    setContentMode,
                    gameData,
                    usersOpen,
                    setUsersOpen,

                    invites,
                    setInvitesOpen,
                    invitesOpen
                }}>
                <div className="app">
                    <Switch>

                        { !user &&
                        <Route>
                            <Login/>
                        </Route>
                        }

                        <Route exact={true} path={"/"} >
                            <Header />
                            <Controls/>

                            <div className={"content " + contentMode.toLowerCase()}>
                                <Content />
                            </div>
                        </Route>

                        <Redirect to="" />
                    </Switch>
                </div>
            </AppContext.Provider>
        );
    }

     function Controls () {

        return (
            <>
                {usersOpen ? <Users/> : <UsersLogo/>}
                {invitesOpen ? <Invites/> : <InvitesLogo/>}
            </>
        );
     }



    function Content() {
        switch (contentMode) {
            case ContentMode.HOME:
                return(<Home/>);
            case ContentMode.CHAT:
                return(<Chat/>);
            case ContentMode.GAME:
                return(<Game/>);
        }

        return null;
    }

    async function fetchUser(){
        let user = await API.getUser() as User;
        log("fetchUser: " + (user && user.name));
        setUser(user);
    }

    async function fetchGameData(){
        let gameData = await API.getGameData() as GameData;
        log("fetchGameData: " + gameData);
        setGameData(gameData);
    }

    async function fetchInvites(){
        let invites = await API.getInvites() as Invite[];
        log("fetchInvites: " + invites);
        setInvites(invites);
    }


    function reconnect(){
        setUser(undefined);
    }

    function sendMessage(data: any, messageType: MessageType){
        log("sendMessage: " + messageType + data);
        let messageDto = JSON.stringify({
            data: JSON.stringify(data),
            messageType: messageType
        } as WSMessage);
        webSocket && webSocket.send(messageDto);
    }

    function setInitialState() {
        log("setInitialState");

        webSocket && webSocket.close();
        setWebsocket(null);

        setChatMessages([]);
        setFriends([]);
        setUsersOpen(false);

        setContentMode(ContentMode.HOME);

        setGameData(null);
        setInvites([]);
    }

    async function createNewWebSocket(){
        log("reConnect");

        let token = await API.getUserToken();

        let newWebSocket = new WebSocket('ws://' + IP + PORT + '/websocket') as WebSocket;

        newWebSocket.onmessage = (json) => {
            try{
                let message = JSON.parse(json.data) as WSMessage;
                log("onMessage: " + message.messageType + message.data);

                switch (message.messageType) {
                    case MessageType.CHAT_MESSAGE.valueOf():
                        setChatMessages(chatMessagesRef.current.concat(JSON.parse(message.data) as ChatMessage));
                        break;
                    case MessageType.FRIEND_LIST.valueOf():
                        let friends = JSON.parse(message.data) as User[];
                        setFriends(friends);
                        break;
                    case MessageType.GAME_DATA.valueOf():
                        setGameData(JSON.parse(message.data) as GameData);
                        break;

                    case MessageType.INVITE_LIST.valueOf():
                        setInvites(JSON.parse(message.data) as Invite[]);
                        break;

                }
            } catch (e) {
                log("ERROR in onMessage");
            }

        };

        newWebSocket.onerror = (response) => {
            log("onError");
            setUser(undefined);
        };

        newWebSocket.onclose = (response) => {
            log("onClose");
            setUser(undefined);
        };

        newWebSocket.onopen = async (response) => {
            log("onOpen");
            newWebSocket.send(
                JSON.stringify({
                    data: JSON.stringify({
                        userId: user!.id,
                        token
                }),
                messageType: MessageType.USER_TOKEN
            } as WSMessage));
        };

        setWebsocket(newWebSocket);
    }


}



export default App;
