import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './app.css';

import {ChatMessage, GameState, User, WSMessage} from "./util/types";
import {Login} from "./login/Login";
import {MessageType} from "./util/enums";
import API from "./util/API";
import {Game, GameType} from "./game/Game";
import {log, WEBSOCKET_URL} from "./index";
import {Header} from "./page/header/Header";
import {Users, UsersLogo} from "./page/controls/Users";
import {Invites, InvitesLogo} from "./page/controls/Invites";
import {Home} from "./home/Home";
import {Chat} from "./chat/Chat";
import {FriendRequests, FriendRequestsLogo} from "./page/controls/FriendRequests";
import {CarcassonneGameSettingsDTO} from "./game/carcassonne/carcassonneTypes";
import {Dev} from "./dev/Dev";

export const AppContext = React.createContext<Partial<ContextProps>>({});

export enum ContentMode  {
    HOME = "HOME",
    CHAT = "CHAT",
    GAME = "GAME",
    DEV = "DEV"
}

export type GameData = {
    startable: Boolean
    gameType: GameType
    gameState: GameState
    players: User[]
    owner: User
    invitedUsers: User[]

    gameJSON: string
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

    addNewFriendOpen: boolean
    setAddNewFriendOpen: (addNewFriendOpen: boolean) => any
    removeFriendActive: boolean
    setRemoveFriendActive: (removeFriendActive: boolean) => any

    invites: User[]
    setInvitesOpen: (invitesOpen: boolean) => any
    invitesOpen: boolean

    friendRequests: User[]
    setFriendRequestsOpen: (invitesOpen: boolean) => any
    friendRequestsOpen: boolean

    gameSettings: any
    setGameSettings: (gameSettings: any) => void
};


export const defaultContentMode = ContentMode.GAME;

export function App () {

    const [user, setUser] = useState<User | null | undefined>(undefined);
    const [webSocket, setWebsocket] = useState<WebSocket | null>(null);
    const [gameData, setGameData] = useState<GameData | null | undefined>(undefined);
    const [friends, setFriends] = useState<User[]>([]);
    const [contentMode, setContentMode] = useState<ContentMode>(defaultContentMode);
    const [chatMessages, _setChatMessages] = useState<ChatMessage[]>([]);
    const chatMessagesRef = React.useRef(chatMessages);
    const setChatMessages = (chatMessages: ChatMessage[]) => {
        chatMessagesRef.current = chatMessages;
        _setChatMessages(chatMessages);
    };
    const [gameTypes, setGameTypes] = useState<GameType[]>([GameType.AMOBA, GameType.CHESS, GameType.CARCASSONNE, GameType.SNAPSZER]); // TODO fetch + cache
    const [usersOpen, setUsersOpen2] = useState<boolean>(false);
    function setUsersOpen(usersOpen: boolean){
        setUsersOpen2(usersOpen);
    }
    const [addNewFriendOpen, _setAddNewFriendOpen] = useState<boolean>(false);
    function setAddNewFriendOpen(addNewFriendOpen: boolean){
        _setAddNewFriendOpen(addNewFriendOpen);
    }
    const [removeFriendActive, _setRemoveFriendActive] = useState<boolean>(false);
    function setRemoveFriendActive(removeFriendActive: boolean){
        _setRemoveFriendActive(removeFriendActive);
    }


    const [invitesOpen, _setInvitesOpen] = useState<boolean>(false);
    function setInvitesOpen(invitesOpen: boolean){
        _setInvitesOpen(invitesOpen);
    }
    const [invites, setInvites] = useState<User[]>();


    const [friendRequestsOpen, _setFriendRequestsOpen] = useState<boolean>(false);
    function setFriendRequestsOpen(friendRequestsOpen: boolean){
        _setFriendRequestsOpen(friendRequestsOpen);
    }
    const [friendRequests, setFriendRequests] = useState<User[]>([]);

    const [gameSettings, _setGameSettings] = useState<any>(null);
    function setGameSettings(gameSettings: any){
        const carcassonneGameSettingsDTO = gameSettings as CarcassonneGameSettingsDTO;
        _setGameSettings(gameSettings);
    }

    // const gameType = gameData && gameData.gameType;
    // useEffect(() => {
    //     setGameSettings(null);
    // }, [gameType]);

    useEffect(() => {
        log("useEffect");

        setInitialState();

        if(user === undefined){
            fetchUser();
            return;
        }

        if (user === null){
            return;
        }

        fetchFriends();

        fetchInvites();

        fetchFriendRequests();

        fetchGameData();

        createNewWebSocket();

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
                    addNewFriendOpen,
                    setAddNewFriendOpen,
                    removeFriendActive,
                    setRemoveFriendActive,
                    invites,
                    setInvitesOpen,
                    invitesOpen,
                    friendRequests,
                    friendRequestsOpen,
                    setFriendRequestsOpen,
                    gameSettings,
                    setGameSettings
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
                            <div className={"content " + contentMode.toLowerCase() + "-content"}>
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
                {friendRequestsOpen ? <FriendRequests/> : <FriendRequestsLogo/>}
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
            case ContentMode.DEV:
                return(<Dev/>);
            default:
                return null;
        }
    }

    async function fetchUser(){
        let user = await API.getUser() as User;
        log("fetchUser: " , user);
        setUser(user);
    }

    async function fetchGameData(){
        let gameData = await API.getGameData() as GameData;
        log("fetchGameData: ", gameData);
        setGameData(gameData);
    }

    async function fetchFriends(){
        let friends = await API.getFriends() as User[];
        log("fetchFriends: " , friends);
        setFriends(friends);
    }

    async function fetchInvites(){
        let invites = await API.getInvites() as User[];
        log("fetchInvites: " , invites);
        setInvites(invites);
    }

    async function fetchFriendRequests(){
        let friendRequests = await API.getFriendRequests() as User[];
        log("fetchFriendRequests: " , friendRequests);
        setFriendRequests(friendRequests);
    }

    function reconnect(){
        setUser(undefined);
    }

    function sendMessage(data: any, messageType: MessageType){
        log("sendMessage: " , messageType , data);
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

        setContentMode(defaultContentMode);

        setGameData(undefined);
        setInvites([]);
    }

    async function createNewWebSocket(){
        log("createNewWebSocket: " + WEBSOCKET_URL);

        let token = await API.getUserToken();

        let newWebSocket = new WebSocket(WEBSOCKET_URL) as WebSocket;

        newWebSocket.onmessage = (json) => {
            try{
                let message = JSON.parse(json.data) as WSMessage;
                log("onMessage: ", message);

                switch (message.messageType) {
                    case MessageType.CHAT_MESSAGE.valueOf():
                        setChatMessages(chatMessagesRef.current.concat(JSON.parse(message.data) as ChatMessage));
                        break;
                    case MessageType.FRIEND_LIST.valueOf():
                        let friends = JSON.parse(message.data) as User[];
                        setFriends(friends);
                        break;
                    case MessageType.GAME.valueOf():
                        setGameData(JSON.parse(message.data) as GameData);
                        break;
                    case MessageType.INVITE_LIST.valueOf():
                        setInvites(JSON.parse(message.data) as User[]);
                        break;
                    case MessageType.FRIEND_REQUEST_LIST.valueOf():
                        setFriendRequests(JSON.parse(message.data) as User[]);
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
