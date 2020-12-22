import React, {ChangeEvent, useContext, useState} from 'react';
import {AppContext, GameData} from "../../App";
import {GameButton} from "../Game";
import {placeholder} from "@babel/types";
import API from "../../util/API";


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

export function Amoba ({}: AmobaProps) {

    //
    // if (lobbyData === undefined){
    //
    // }
    //
    // if (gameData){
    //     return AmobaGame();
    // } else if(lobbyData){
    //     return AmobaLobby();
    // } else {
    //     return null;
    // }
    //
    // function AmobaLobby () {
    //
    //     const { user } = useContext(AppContext);
    //
    //     return (
    //         <div>
    //             {lobbyData!.users.map((user, key)=>
    //                 <GameButton enabled={true} key={key} text={user.name} onClick={()=> {}}/>
    //             )}
    //             {
    //                 user!.id == lobbyData!.user.id && AmobaSettings()
    //             }
    //         </div>
    //     );
    //
    // }
    //
    // function AmobaSettings () {
    //
    //     enum MeAs {
    //         X = "Me as: X",
    //         O = "Me as: O",
    //         R = "Me as: Random"
    //     }
    //
    //     const [meAs, setMeAs] = useState<MeAs>(MeAs.R);
    //
    //     return (
    //         <>
    //             <select className={"game-button"} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setMeAs(e.target.value as MeAs)}} value={meAs}>
    //                 <option value = {MeAs.X}>{MeAs.X}</option>
    //                 <option value = {MeAs.O}>{MeAs.O}</option>
    //                 <option value = {MeAs.R}>{MeAs.R}</option>
    //             </select>
    //             {/*<StartGameButton enabled={startable()} lobbyData={{gameType: GameType.AMOBA} as LobbyData}/>*/}
    //             {/*<StartGameButton enabled={startable()} lobbyData={{gameType: GameType.AMOBA, ...lobbyData} as LobbyData}/>*/}
    //         </>
    //     );
    //
    //     function startable() :boolean{
    //         return lobbyData!.users.length == 2;
    //     }
    //
    // }



    return (
        <div>AMOBA_GAME</div>
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

    function setOwnerAs(ownerAs: OwnerAs) {
        const amobaLobbyData = {
            ownerAs: ownerAs
        } as AmobaLobbyProps;

        API.sendLobbyData(amobaLobbyData);
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
                gameData.owner.id === user!.id && <GameButton text={"START"} onClick={()=> {}} enabled={!!gameData.startable}/>
            }

        </div>
    );


}


