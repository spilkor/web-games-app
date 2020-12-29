import React, {useContext} from 'react';
import '../css/friends.css';
import {Modal} from "./Modal";
import {ReactComponent as Logo} from '../svg/questionmark.svg';
import {ReactComponent as AcceptInviteLogo} from '../svg/acceptinvite.svg';

import {User} from "../util/types";
import {AppContext} from "../App";
import API from "../util/API";


export function FriendRequestsLogo(){

    const { friendRequestsOpen, setFriendRequestsOpen, friendRequests } = useContext(AppContext);

    const hasFriendRequest = friendRequests!.length != 0;

    if (hasFriendRequest || friendRequestsOpen){
        return(
            <div className={"friend-requests-logo" + (friendRequestsOpen ? " open" : "")} onClick={()=> setFriendRequestsOpen!(!friendRequestsOpen)}>
                <Logo/>
            </div>
        );
    } else {
        return null;
    }

}


export function FriendRequests () {

    const { setFriendRequestsOpen, friendRequests} = useContext(AppContext);

    return (
        <Modal isOpen={true} closeOnBackGroundClick={true} close={()=> {setFriendRequestsOpen!(false)}}>

            <div className={"friend-requests"}>
                <div className={"friend-request-list"}>

                    {friendRequests!.map((friendRequest, key) =>
                        <FriendRequestCard key = {key} friend={friendRequest} />
                    )}

                </div>

                <FriendRequestsLogo/>
            </div>

        </Modal>
    );

    function FriendRequestCard({friend}: FriendRequestCardProps) {

        return (
            <div className={"friend-request-card"}>
                <span className={"friend-name"}>{friend.name}</span>
                <Accept/>
                {/*<Decline/>*/}
            </div>
        );

        function Accept() {

            return (
                <div className={"accept-invite-logo"} onClick={()=> accept()}>
                    <AcceptInviteLogo/>
                </div>
            );
        }

        async function accept() {
            await API.requestFriend(friend.name);
        }

        // function Decline() {
        //
        //     return (
        //         <div className={"decline-invite-logo"} onClick={()=> decline()}>
        //             <DeclineInviteLogo/>
        //         </div>
        //     );
        // }
        //
        //
        // async function decline() {
        //     await API.decline(invite.id);
        // }
    }


    type FriendRequestCardProps = {
        friend: User
    }

}

















