import React, {useContext, useEffect, useRef, useState} from 'react';
import {AppContext, ContentMode, defaultContentMode} from "../App";
import {Key} from "ts-keycode-enum";
import API from "../util/API";
import {Redirect, Route, Switch} from "react-router";
import {Login} from "../login/Login";
import {Header} from "./header/Header";
import {Home} from "../home/Home";
import {Chat} from "../chat/Chat";
import {Game} from "../game/Game";
import {Dev} from "../dev/Dev";
import {Users, UsersLogo} from "./controls/Users";
import {Invites, InvitesLogo} from "./controls/Invites";
import {FriendRequests, FriendRequestsLogo} from "./controls/FriendRequests";


export function Page () {

    const [contentMode, setContentMode] = useState<ContentMode>(defaultContentMode);

    const { user } = useContext(AppContext);

    return (
        <div className={"page"}>
            {
                !user ?
                    <Login/>
                    :
                    <>
                        <Header />
                        <Controls/>
                        <div className={"content " + contentMode.toLowerCase() + "-content"}>
                            <Content {...contentMode}/>
                        </div>
                    </>
            }
        </div>
    );

    function Controls () {
        return (
            <>
                {/*{usersOpen ? <Users/> : <UsersLogo/>}*/}
                {/*{invitesOpen ? <Invites/> : <InvitesLogo/>}*/}
                {/*{friendRequestsOpen ? <FriendRequests/> : <FriendRequestsLogo/>}*/}
            </>
        );
    }

    function Content(contentMode: ContentMode) {
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
                return (<Content {...defaultContentMode}/>);
        }
    }

}



