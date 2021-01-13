import React, {useContext, useEffect, useState} from 'react';
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

    const { setUsersOpen, usersOpen, setAddNewFriendOpen, setRemoveFriendActive} = useContext(AppContext);

    return(
        <div className={"friends-logo" + (usersOpen ? " open" : "")} onClick={()=> {setAddNewFriendOpen!(false);setRemoveFriendActive!(false);setUsersOpen!(!usersOpen)}}>
            <FriendsLogo/>
        </div>
    );
}

export function Users () {

    const { usersOpen, setUsersOpen, addNewFriendOpen, setAddNewFriendOpen, removeFriendActive, setRemoveFriendActive, friends, gameData, user} = useContext(AppContext);

    const addFriendLogo =
        <div className={"add-friend-logo"} onClick={()=> {setAddNewFriendOpen!(!addNewFriendOpen);setRemoveFriendActive!(false)}}>
            <PlusLogo/>
        </div>;
    const removeFriendLogo =
        <div className={"remove-friend-logo" + (removeFriendActive ? " active" : "")} onClick={()=> {setRemoveFriendActive!(!removeFriendActive);setAddNewFriendOpen!(false)}}>
            <MinusLogo40/>
        </div>;

    type Invite = {
        owner: User,
        friend: User
    }

    return (
        <div className={"friends"}>
            <Modal isOpen={!!usersOpen} closeOnBackGroundClick={true} close={()=> {setUsersOpen!(false); setAddNewFriendOpen!(false); setRemoveFriendActive!(false)}}>
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


    type UserCardProps = {
        cardUser: User
    }

    function UserCard({cardUser}: UserCardProps) {

        const { user }  = useContext(AppContext);
        const isRemove = removeFriendActive && cardUser.id !== user!.id;

        return (
            <div className={"friend-card" + (isRemove ? " remove" : "")} onClick={()=> isRemove && removeFriend()}>
                <div className={"name-and-crown"}>
                    <div className={"friend-name"}>{cardUser!.name}</div>
                    {isRemove || <Crown/>}
                </div>

                {isRemove ||
                <>
                    <Leave/>
                    <Kick/>
                    <Invite/>
                    <UserStateSign/>
                </>
                }

            </div>
        );

        async function removeFriend() {
            await API.removeFriend(cardUser.id);
        }

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
            if (gameData && gameData.gameState !== GameState.IN_GAME && gameData.owner.id === user!.id && gameData.owner.id !== cardUser.id && gameData.players.filter((player) => player.id === cardUser.id ).length == 1){
                return (
                    <div className={"friend-sign"} onClick={()=> kickPlayer()}>
                        <div className={"kick"}>
                            <KickLogo/>
                        </div>
                    </div>
                );
            } else {
                return null;
            }

            async function kickPlayer() {
                await API.kickPlayer(cardUser.id);
            }
        }

        function Leave() {
            if (gameData && gameData.gameState !== GameState.IN_GAME && cardUser.id === user!.id){
                return (
                    <div className={"friend-sign"} onClick={()=> leaveGame()}>
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
        }

        function Invite() {

            if (gameData && gameData.gameState === GameState.IN_LOBBY &&  gameData.owner.id === user!.id && gameData.players.filter((player) => player.id === cardUser.id ).length == 0 && gameData.invitedUsers.filter((player) => player.id === cardUser.id ).length == 0){
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

        function UserStateSign() {

            let friend = friends && friends.find((f)=>{return f.id === cardUser.id});
            let userState = user!.id === cardUser.id ? UserState.ONLINE : friend ? friend.userState : cardUser.userState;

            return(
                <div className={"friend-sign"}>
                    <div className={"friend-state " + (userState.toString())}/>
                </div>
            );

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
                setAddNewFriendOpen!(false);
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

















