import React, {useContext, useState} from 'react';
import '../css/friends.css';
import {Modal} from "./Modal";
import {ReactComponent as Logo} from '../svg/questionmark.svg';
import {ReactComponent as PlusLogo} from '../svg/plus2.svg';

import {ReactComponent as InviteLogo} from '../svg/invite.svg';

import {ReactComponent as KickLogo} from '../svg/kick.svg';

import {ReactComponent as MinusLogo40} from '../svg/minus40.svg';
import {ReactComponent as AcceptInviteLogo} from '../svg/acceptinvite.svg';

import {ReactComponent as DeclineInviteLogo} from '../svg/declineinvite.svg';



import {ReactComponent as Pawn} from '../svg/pawn2.svg';

import {User, UserState} from "../util/types";
import {AppContext} from "../App";
import API from "../util/API";
import {placeholder} from "@babel/types";
import {func} from "prop-types";

export type Invite = {
    owner: User,
    friend: User
}



export function InvitesLogo(){

    const { user, invitesOpen, setInvitesOpen, invites} = useContext(AppContext);

    const hasInvite = invites && invites.filter(i=>i.owner.id !== user!.id).length != 0;

    if (hasInvite || invitesOpen){
        return(
            <div className={"invites-logo " + (hasInvite && " yellow")} onClick={()=> {(hasInvite || invitesOpen) && setInvitesOpen!(!invitesOpen)}}>
                <Logo/>
            </div>
        );
    } else {
        return null;
    }

}


export function Invites () {

    const { user, gameData, setInvitesOpen, invites} = useContext(AppContext);



    return (
        <Modal isOpen={true} closeOnBackGroundClick={true} close={()=> {setInvitesOpen!(false)}}>

            <div className={"invites"}>
                <div className={"invite-list"}>

                    {invites && invites.filter((i) => i.owner.id !== user!.id).map((invite, key) =>
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
                <span className={"friend-name"}>{invite.owner.name}</span>
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

        function accept() {
            API.accept(invite.owner.id);
        }

        function Decline() {

            return (
                <div className={"decline-invite-logo"}>
                    <DeclineInviteLogo/>
                </div>
            );
        }
    }


    type InviteCardProps = {
        invite: Invite
    }

}

















