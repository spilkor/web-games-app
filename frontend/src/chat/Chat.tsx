import React, {useContext, useEffect, useRef, useState} from 'react';
import {AppContext} from "../App";
import {Key} from "ts-keycode-enum";
import API from "../util/API";


export function Chat () {

    const [text, _setText] = useState("");
    const {chatMessages, user} = useContext(AppContext);

    const textRef = React.useRef(text);
    const setText = (text: string) => {
        textRef.current = text;
        _setText(text);
    };

    const listener = (event: KeyboardEvent) => {
        if (event.keyCode === Key.Enter && textRef.current != "") {
            API.sendChatMessage(textRef.current);
            setText("");
        }
    };

    useEffect(() => {
        window.addEventListener('keyup', listener);
        return () => {
            window.removeEventListener('keyup', listener);
        };
    }, []);


    const messagesEndRef = useRef<HTMLDivElement>(null)

    const scrollToBottom = () => {
        messagesEndRef && messagesEndRef.current && messagesEndRef.current.scrollIntoView!()
    };

    useEffect(scrollToBottom, [chatMessages]);

    return (
        <div className={"chat"}>
            <div className={"messages"}>
                {chatMessages!.map((chatMessage, key)=>
                    <div key={key} className={"message-box" + (user!.id === chatMessage.user.id ? " mine" : "")}>
                        <div className={"message-user"}>
                            {chatMessage.user.name}
                        </div>
                        <div className={"message-text"}>
                            {chatMessage.message}
                        </div>
                    </div>
                )}
                <div ref={messagesEndRef} />
            </div>
            <div className={"user-input"}>
                <input value={text} onChange={(event) => setText(event.target.value)} autoFocus={true}/>
            </div>
        </div>
    );

}



