import React, {ChangeEvent, useContext} from 'react';
import {AppContext} from "../../App";
import {QuitButton, StartGameButton, SystemMessage} from "../Game";
import API from "../../util/API";
import {GameState, User} from "../../util/types";

enum OwnerAs {
    Random = "Random",
    X = "X",
    O = "O"
}

export function Amoba () {

    type AmobaLobbyDTO = {
        ownerAs: OwnerAs
    }

    type AmobaGameDTO = {
        nextPlayer: User | null,
        table: Boolean[]
    }

    type AmobaEndDTO = {
        winner: User | null
    }

    type AmobaMoveDTO = {
        index: number
    }

    const { user, groupData } = useContext(AppContext);
    const gameData = JSON.parse(groupData!.gameJSON) as AmobaGameDTO;
    const lobbyData = JSON.parse(groupData!.lobbyJSON) as AmobaLobbyDTO;
    const endData = JSON.parse(groupData!.endJSON) as AmobaEndDTO;
    const myMove = gameData && gameData.nextPlayer && gameData.nextPlayer.id === user!.id;

    return(
        <div className={"amoba"}>
            <Content/>
        </div>
    );

    function Content () {
        switch (groupData!.gameState) {
            case GameState.IN_LOBBY:
                return(
                    <AmobaLobby/>
                );
            case GameState.IN_GAME:
                return(
                    <AmobaGame/>
                );
            case GameState.GAME_END:
                return(
                    <AmobaEnd/>
                );
            default:
                return null;
        }
    }

    function AmobaLobby () {
        return (
            <div className={"lobby"}>
                <div className={"caption"}>AmobaLobby</div>
                <div className={"row"}>
                    <div className={"key"}>
                        Owner:
                    </div>
                    <div className={"value"}>
                        <span>{groupData!.owner.name}</span>
                    </div>
                    <div className={"key"}>
                        as:
                    </div>
                    <div className={"value"}>
                        <select disabled={! (user!.id == groupData!.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setOwnerAs(e.target.value as OwnerAs)}} value={lobbyData.ownerAs}>
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
                        {groupData!.players.map((user, key)=>
                            <div><span>{user.name}</span></div>
                        )}
                    </div>
                </div>
                {groupData!.owner.id === user!.id &&
                    <StartGameButton text={"START"} enabled={user!.id === groupData!.owner.id && groupData!.startable === true}/>
                }
            </div>
        );

        function setOwnerAs(ownerAs: OwnerAs) {
            const amobaLobbyData = {
                ownerAs: ownerAs
            } as AmobaLobbyDTO;

            API.sendLobbyData(JSON.stringify(amobaLobbyData));
        }

    }

    function AmobaGame () {
        return (
            <div className={"game"}>
                <Table/>
                <SystemMessage text = {gameData.nextPlayer && gameData.nextPlayer.id === user!.id ? "Your move" : "Waiting for opponent"}/>
            </div>
        );
    }

    function AmobaEnd () {
        return (
            <div className={"end"}>
                <Table/>
                <SystemMessage text = {!endData.winner ? "Draw" : endData.winner.id === user!.id ? "You won" : "You lost"}/>
                <QuitButton/>
            </div>
        );
    }

    function Table() {
        return (
            <div className={"table"}>
                <div className={"row"}>
                    <Squire index={0}/>
                    <Squire index={1}/>
                    <Squire index={2}/>
                </div>
                <div className={"row"}>
                    <Squire index={3}/>
                    <Squire index={4}/>
                    <Squire index={5}/>
                </div>
                <div className={"row"}>
                    <Squire index={6}/>
                    <Squire index={7}/>
                    <Squire index={8}/>
                </div>
            </div>
        );
    }

    type SquireProps = {
        index: number
    }

    function Squire({index}: SquireProps) {
        const value = gameData.table[index];
        const clickable = myMove && value === null;
        const size = 50;

        return(
            <div className={"squire" + (clickable ? " clickable" : "")} onClick={()=> clickable && move()}>
                {value === true && <Squire_X/>}
                {value === false && <Squire_O/>}
            </div>
        );

        function move() {
            let amobaMoveDTO = { index } as AmobaMoveDTO;
            API.move(JSON.stringify(amobaMoveDTO));
        }

        function Squire_X() {
            return(
                <svg height={size} width={size} className={"amoba_x"}>
                    <line x1={size/10}  y1={size/10}  x2={size/10*9}  y2={size/10*9} strokeWidth={size/5} strokeLinecap="round" />
                    <line x1={size/10} y1={size/10*9} x2={size/10*9} y2={size/10} strokeWidth={size/5} strokeLinecap="round" />
                </svg>
            );
        }

        function Squire_O() {
            return(
                <svg className="amoba_o">
                    <circle cx={size/2} cy={size/2} strokeWidth={size/5} r={size/100*40} />
                </svg>
            );
        }
    }

}


