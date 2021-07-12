import React, {useContext} from 'react';
import './controls.css';
import {ReactComponent as Logo} from '../../svg/questionmark.svg';
import {ReactComponent as AcceptInviteLogo} from '../../svg/acceptinvite.svg';

import {ReactComponent as DeclineInviteLogo} from '../../svg/declineinvite.svg';

import {GameState, User} from "../../util/types";
import {AppContext, ContentMode} from "../../App";
import API from "../../util/API";
import {Modal} from "../../modal/Modal";

export type Invite = {
    owner: User,
    friend: User
}



export function InvitesLogo(){

    const { user, invitesOpen, setInvitesOpen, invites, gameData} = useContext(AppContext);

    const hasInvite = invites && invites.length != 0;
    // const gameInProgress = gameData && gameData.gameState === GameState.IN_GAME;

    if (hasInvite || invitesOpen && !gameData){
        return(
            <div className={"invites-logo" + (hasInvite && " default_yellow") + (invitesOpen ? " open" : "")} onClick={()=> setInvitesOpen!(!invitesOpen)}>
                <Logo/>
            </div>
        );
    } else {
        return null;
    }

}


export function Invites () {

    const { user, setInvitesOpen, invites, setContentMode} = useContext(AppContext);

    return (
        <Modal isOpen={true} closeOnBackGroundClick={true} close={()=> {setInvitesOpen!(false)}}>

            <div className={"invites"}>
                <div className={"invite-list"}>

                    {invites && invites.map((invite, key) =>
                        <InviteCard key = {key} invite={invite} />
                    )}

                </div>

                <InvitesLogo/>
            </div>

        </Modal>
    );

    function InviteCard({invite}: InviteCardProps) {

        return (
            <div className={"invite-card"}>
                <span className={"friend-name"}>{invite.name}</span>
                <Accept/>
                <Decline/>
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
            await API.accept(invite.id);
            setInvitesOpen!(false);
            setContentMode!(ContentMode.GAME);
        }

        function Decline() {

            return (
                <div className={"decline-invite-logo"} onClick={()=> decline()}>
                    <DeclineInviteLogo/>
                </div>
            );
        }


        async function decline() {
            await API.decline(invite.id);
        }
    }


    type InviteCardProps = {
        invite: User
    }

}

















