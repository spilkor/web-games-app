import React, {ChangeEvent, useContext, useState} from 'react';
import {AppContext, GameData} from "../../App";
import {GameButton} from "../Game";
import {placeholder} from "@babel/types";
import API from "../../util/API";
import {User} from "../../util/types";


type AmobaProps = {

}

enum OwnerAs {
    Random = "Random",
    X = "X",
    O = "O"
}

type AmobaLobbyProps = {
    ownerAs: OwnerAs
}

type AmobaGameDTO = {
    nextPlayer: User,
    table: Boolean[][]
}

export function Amoba (amobaGameDTO: AmobaGameDTO) {

    // const amobaGameData = JSON.parse(gameData.gameJSON) as AmobaGameDTO;
    // console.log("amobaGameData: ", amobaGameData);

    return (
        <div>AMOBA_GAME{amobaGameDTO.table.toString()}</div>
    );


}
function AmobaGame () {

    return (
        <div>AMOBA_GAME</div>
    );


}

export function AmobaLobby (gameData: GameData) {

    const { user , setUsersOpen} = useContext(AppContext);

    const amobaLobbyData = JSON.parse(gameData.lobbyJSON) as AmobaLobbyProps;
    console.log("amobaLobbyData: ", amobaLobbyData);


    if (amobaLobbyData == null){
        return null;
    }

    return (
        <div className={"lobby"}>
            <div className={"caption"}>AmobaLobby</div>

            <div className={"row"}>
                <div className={"key"}>
                    Owner:
                </div>
                <div className={"value"}>
                    <span>{gameData.owner.name}</span>
                </div>

                <div className={"key"}>
                    as:
                </div>
                <div className={"value"}>
                    <select disabled={! (user!.id == gameData.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setOwnerAs(e.target.value as OwnerAs)}} value={amobaLobbyData.ownerAs}>
                        <option value = {OwnerAs.Random}>{OwnerAs.Random}</option>
                        <option value = {OwnerAs.X}>{OwnerAs.X}</option>
                        <option value = {OwnerAs.O}>{OwnerAs.O}</option>
                    </select>
                </div>
            </div>

            <div className={"row"}>
                <div className={"key"}>
                    Players:
                </div>
                <div className={"value"}>
                    {gameData.players.map((user, key)=>
                        <div><span>{user.name}</span></div>
                    )}
                </div>
            </div>

            <div className={"row"}>
                <div className={"key"}>
                    <button onClick={()=>{setUsersOpen!(true)}}>Invite</button>
                </div>
                <div className={"value"}>
                </div>
            </div>

            {
                gameData.owner.id === user!.id && <GameButton text={"START"} onClick={()=> {API.startGame()}} enabled={true}/>
            }

        </div>
    );

    function setOwnerAs(ownerAs: OwnerAs) {
        const amobaLobbyData = {
            ownerAs: ownerAs
        } as AmobaLobbyProps;

        API.sendLobbyData(amobaLobbyData);
    }

}


