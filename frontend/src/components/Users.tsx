import React, {useContext, useState} from 'react';
import '../css/friends.css';
import {Modal} from "./Modal";
import {ReactComponent as FriendsLogo} from '../svg/friends.svg';
import {ReactComponent as PlusLogo} from '../svg/plus2.svg';

import {ReactComponent as InviteLogo} from '../svg/invite.svg';

import {ReactComponent as KickLogo} from '../svg/kick.svg';

import {ReactComponent as MinusLogo40} from '../svg/minus40.svg';
import {ReactComponent as PlusLogo70} from '../svg/plus70.svg';
import {ReactComponent as CrownLogo} from '../svg/crown.svg';

import {GameState, User, UserState} from "../util/types";
import {AppContext} from "../App";
import API from "../util/API";


export function UsersLogo(){

    const { setUsersOpen, usersOpen, invites} = useContext(AppContext);

    return(
        <div className={"friends-logo"} onClick={()=> {setUsersOpen!(!usersOpen)}}>
            <FriendsLogo/>
        </div>
    );
}

export function Users () {

    const { usersOpen, setUsersOpen} = useContext(AppContext);

    const [addNewFriendOpen, setAddNewFriendOpen] = useState<boolean>(false);
    const [removeFriendActive, setRemoveFriendActive] = useState<boolean>(false);
    const {friends, gameData, user}  = useContext(AppContext);
    const addFriendLogo =
        <div className={"add-friend-logo"} onClick={()=> {setAddNewFriendOpen(!addNewFriendOpen);setRemoveFriendActive(false)}}>
            <PlusLogo/>
        </div>;
    const removeFriendLogo =
        <div className={"remove-friend-logo" + (removeFriendActive ? " active" : "")} onClick={()=> {setRemoveFriendActive(!removeFriendActive);setAddNewFriendOpen(false)}}>
            <MinusLogo40/>
        </div>;

    type Invite = {
        owner: User,
        friend: User
    }

    return (
        <div className={"friends"}>
            <Modal isOpen={!!usersOpen} closeOnBackGroundClick={true} close={()=> {setUsersOpen!(false); setAddNewFriendOpen(false); setRemoveFriendActive(false)}}>
                <div className={"friend-list"}>
                    <div className={"lobby-background"}>

                        <UserCard cardUser={user!}/>

                        {gameData && gameData.owner.id !== user!.id &&
                        <UserCard cardUser={gameData.owner!}/>
                        }

                        {gameData && gameData.players.filter((player) => player.id !== user!.id && player.id !== gameData.owner.id).sort((a, b) => a.name < b.name ? 1 : -1).sort((a, b) => a.userState===UserState.ONLINE ? -1 : 1).map((player, key) =>
                            <UserCard key={key} cardUser={player} />
                        )}
                    </div>

                    <div className={"friends-background"}>
                        {friends && friends.filter((friend) => !gameData || gameData.players.filter((p)=> p.id === friend.id).length === 0).sort((a, b) => a.name < b.name ? 1 : -1).sort((a, b) => a.userState===UserState.ONLINE ? -1 : 1).map((friend, key) =>
                            <UserCard key={key} cardUser={friend} />
                        )}
                    </div>
                </div>



                {removeFriendLogo}
                {addFriendLogo}
                <FriendsLogo/>

                {addNewFriendOpen && <AddNewFriendComp/>}

                <UsersLogo/>

            </Modal>

        </div>
    );

    function InviteCard({invite}: InviteCardProps) {

        return (
            <div className={"invite-card"}>
                <span className={"friend-name"}>{invite.friend.name}</span>

                <Accept/>
                <Decline/>
            </div>
        );

        function Accept() {

            return (
                <div>
                    ACCEPT
                </div>
            );
        }

        function Decline() {

            return (
                <div>
                    DECLINE
                </div>
            );
        }
    }

    async function removeFriend(friend: User) {
        await API.removeFriend(friend.id);
    }



    type UserCardProps = {
        cardUser: User
    }


    type InviteCardProps = {
        invite: Invite
    }

    function UserCard({cardUser}: UserCardProps) {

        const { user }  = useContext(AppContext);

        return (
            <div className={"friend-card" + (removeFriendActive ? " remove" : "")} onClick={()=> removeFriendActive && removeFriend(cardUser)}>
                <div className={"name-and-crown"}>
                    <div className={"friend-name"}>{cardUser!.name}</div>
                    <Crown/>
                </div>

                <Kick/>
                <Invite/>
                <UserState/>
            </div>
        );

        function Crown() {
            if (gameData && gameData.owner.id === cardUser.id){
                return (
                    <div>
                        <div className={"crown"}>
                            <CrownLogo/>
                        </div>
                    </div>
                );
            } else {
                return null;
            }
        }

        function Kick() {
            if (gameData && gameData.owner.id === user!.id && gameData.players.filter((player) => player.id === cardUser.id ).length == 1){
                return (
                    <div className={"friend-sign"} onClick={()=> {cardUser.id === user!.id ? leaveGame() : kickPlayer()}}>
                        <div className={"kick"}>
                            <KickLogo/>
                        </div>
                    </div>
                );
            } else {
                return null;
            }

            async function leaveGame() {
                await API.leaveGame();
            }

            async function kickPlayer() {
                await API.kickPlayer(cardUser.id);
            }
        }

        function Invite() {

            if (gameData && gameData.gameState !== GameState.IN_GAME &&  gameData.owner.id === user!.id && gameData.players.filter((player) => player.id === cardUser.id ).length == 0 && gameData.invitedUsers.filter((player) => player.id === cardUser.id ).length == 0){
                return (
                    <div className={"friend-sign"}>
                        <div className={"invite"} onClick={()=>{invite()}}>
                            <InviteLogo/>
                        </div>
                    </div>
                );
            } else {
                return null;
            }

            function invite() {
                API.invite(cardUser.id);
            }
        }

        function UserState() {

            let friend = friends && friends.find((f)=>{return f.id === cardUser.id});
            let userState = friend ? friend.userState : cardUser.userState;

            return(
                <div className={"friend-sign"}>
                    <div className={"friend-state " + (userState.toString())}/>
                </div>
            );

            function UserSign(contant: any) {

                return (
                    <div className={"friend-sign"}>
                        <div>
                            {contant}
                        </div>
                    </div>
                );
            }
        }


    }




    function AddNewFriendComp() {

        const [error, setError] = useState<string>("");

        function isFilled(){
            return userNameRef.current != "";
        }

        const [userName, _setUserName] = useState("");
        const userNameRef = React.useRef(userName);
        const setUserName = (userName: string) => {
            userNameRef.current = userName;
            _setUserName(userName);
        };

        async function addNewFriend(userName: string) {
            let error = await API.requestFriend(userName);
            if(!error){
                setUserName("");
                setAddNewFriendOpen(false);
            } else {
                setError(error.message);
            }
        }

        return (
            <div className={"add-new-friend-wrapper"}>
                <div className={"add-new-friend" + (isFilled() ? " filled" : "")}  onClick={()=> isFilled() && addNewFriend(userNameRef.current)}>
                    <div className={"input-with-default-value user-name"}>
                        <input value={userName} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserName(e.target.value)}/>
                        <div></div>
                    </div>
                    <div className={"plus-sign"}>
                        <PlusLogo70/>
                    </div>
                </div>

                {error &&
                <div className={"error"} onClick={()=> setError("")}>
                    {error}
                </div>
                }
            </div>

        );
    }



}

















