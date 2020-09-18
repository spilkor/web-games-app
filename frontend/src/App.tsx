import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/app.css';

import {ChatMessage, User, WSMessage} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import API from "./util/API";

import {Header} from "./components/Header";
import {Chat} from "./components/Chat";
import {Friends} from "./components/Friends";
import {Home} from "./components/Home";
import {IP, log, PORT} from "./index";


export const AppContext = React.createContext<Partial<ContextProps>>({});

export type ContextProps = {
    user: User | null,
    reconnect: () => void,
    chatMessages: ChatMessage[],
    friends: User[],
    setContentMode: (contentMode: ContentMode) => void
};

export enum ContentMode  {
    HOME, CHAT
}

export function App () {
    const [user, setUser] = useState<User | null | undefined>(undefined);
    const [friends, setFriends] = useState<User[]>([]);
    const [webSocket, setWebsocket] = useState<WebSocket | null>(null);
    const [contentMode, setContentMode] = useState<ContentMode>(ContentMode.HOME);
    const [chatMessages, _setChatMessages] = useState<ChatMessage[]>([]);
    const chatMessagesRef = React.useRef(chatMessages);
    const setChatMessages = (chatMessages: ChatMessage[]) => {
        chatMessagesRef.current = chatMessages;
        _setChatMessages(chatMessages);
    };
    const [isOpen, setIsOpen] = useState<boolean>(true);

    useEffect(() => {
        if (user === null){
            return;
        }

        if(user === undefined){
            setInitialState();
            fetchUser();
            return;
        }

        reConnect();
    }, [user]);

    if(user === undefined){
        return null;
    }

    return (
        <AppContext.Provider
            value={{
                user,
                chatMessages,
                reconnect: ()=>{setUser(undefined)},
                friends,
                setContentMode
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
                        <Friends/>
                        <div className={"content"}>
                            <Content />
                        </div>
                    </Route>

                    <Redirect to="" />
                </Switch>
            </div>
        </AppContext.Provider>
    );

    function Content() {
        switch (contentMode) {
            case ContentMode.HOME:
                return(<Home/>);
            case ContentMode.CHAT:
                return(<Chat/>);
        }

        return null;
    }

    async function fetchUser(){
        let user = await API.getUser() as User;
        log("fetchUser: " + (user && user.name));
        setUser(user);
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

        setUser(undefined);
        webSocket && webSocket.close();
        setWebsocket(null);

        setChatMessages([]);
        setFriends([]);
    }

    async function reConnect(){
        log("reConnect");

        webSocket && webSocket.close();

        if(!user){
            setWebsocket(null);
            return;
        }

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
                        setFriends(JSON.parse(message.data) as User[]);
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
                        userId: user.id,
                        token
                }),
                messageType: MessageType.USER_TOKEN
            } as WSMessage));
        };

        setWebsocket(newWebSocket);
    }


}



export default App;
