import React, {useContext, useEffect} from 'react';
import API from "../../util/API";
import {AppContext, ContentMode} from "../../App";
import '../header/header.css';
import {ReactComponent as LogoutLogo} from '../svg/logout.svg';
import {ReactComponent as ReconnectLogo} from '../svg/reconnect.svg';
import {Key} from "ts-keycode-enum";


type MenuButtonProps = {
    text: string
    contentMode: ContentMode
}

export function Controls () {
    return (
        <>
            {/*{usersOpen ? <Users/> : <UsersLogo/>}*/}
            {/*{invitesOpen ? <Invites/> : <InvitesLogo/>}*/}
            {/*{friendRequestsOpen ? <FriendRequests/> : <FriendRequestsLogo/>}*/}
        </>
    );
}









