import React, {ChangeEvent, useContext} from 'react';
import {AppContext} from "../App";
import {QuitButton, StartGameButton, SystemMessage} from "../components/Game";
import API from "../util/API";
import {GameState, User} from "../util/types";

import './amoba.scss';

enum OwnerAs {
    Random = "Random",
    X = "X",
    O = "O"
}

type AmobaLobbyDTO = {
    ownerAs: OwnerAs
}

type AmobaGameDTO = {
    nextPlayer: User | null,
    table: Boolean[],
    ownerAs: OwnerAs,
    winner: User,
    nextSign: Boolean
}

type AmobaMoveDTO = {
    index: number
}

export function Amoba () {

    const { user, gameData } = useContext(AppContext);
    const amobaGameDTO = JSON.parse(gameData!.gameJSON) as AmobaGameDTO;
    const myMove = amobaGameDTO && amobaGameDTO.nextPlayer && amobaGameDTO.nextPlayer.id === user!.id;

    return(
        <div className={"amoba"}>
            <Content/>
        </div>
    );

    function Content () {
        switch (gameData!.gameState) {
            case GameState.IN_LOBBY:
                return(
                    <AmobaLobby/>
                );
            case GameState.IN_GAME:
                return(
                    <AmobaGame/>
                );
            case GameState.ENDED:
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
                        <span>{gameData!.owner.name}</span>
                    </div>
                    <div className={"key"}>
                        as:
                    </div>
                    <div className={"value"}>
                        <select disabled={! (user!.id == gameData!.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setOwnerAs(e.target.value as OwnerAs)}} value={amobaGameDTO.ownerAs}>
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
                        {gameData!.players.map((user, key)=>
                            <div key={key}><span>{user.name}</span></div>
                        )}
                    </div>
                </div>
                {gameData!.owner.id === user!.id &&
                    <StartGameButton text={"START"} enabled={gameData!.startable === true}/>
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
        const nextSign = amobaGameDTO.nextSign ? "X" : "O";
        return (
            <div className={"game"}>
                <Table/>
                <SystemMessage text = {amobaGameDTO.nextPlayer && amobaGameDTO.nextPlayer.id === user!.id ? ("Your move (" + nextSign + ")") : ("Waiting for opponent (" + nextSign + ")")}/>
            </div>
        );
    }

    function AmobaEnd () {
        return (
            <div className={"end"}>
                <Table/>
                <SystemMessage text = {!amobaGameDTO.winner ? "Game ended in a draw" : amobaGameDTO.winner.id === user!.id ? "You won" : "You lost"}/>
                <QuitButton/>
            </div>
        );
    }

    function Table() {
        return (
            <table>
                <tr>
                    <Squire index={0}/>
                    <Squire index={1}/>
                    <Squire index={2}/>
                </tr>
                <tr>
                    <Squire index={3}/>
                    <Squire index={4}/>
                    <Squire index={5}/>
                </tr>
                <tr>
                    <Squire index={6}/>
                    <Squire index={7}/>
                    <Squire index={8}/>
                </tr>
            </table>
        );
    }

    type SquireProps = {
        index: number
    }

    function Squire({index}: SquireProps) {
        const value = amobaGameDTO.table[index];
        const clickable = myMove && value === null;
        const size = 50;

        return(
            <td className={clickable ? " clickable" : ""} onClick={()=> clickable && move()}>
                {value === true && <Squire_X/>}
                {value === false && <Squire_O/>}
            </td>
        );

        function move() {
            let amobaMoveDTO = { index } as AmobaMoveDTO;
            API.move(JSON.stringify(amobaMoveDTO));
        }

        function Squire_X() {
            return(
                <svg height={size} width={size}>
                    <line x1={size/10}  y1={size/10}  x2={size/10*9}  y2={size/10*9} strokeWidth={size/5} strokeLinecap="round" />
                    <line x1={size/10} y1={size/10*9} x2={size/10*9} y2={size/10} strokeWidth={size/5} strokeLinecap="round" />
                </svg>
            );
        }

        function Squire_O() {
            return(
                <svg>
                    <circle cx={size/2} cy={size/2} strokeWidth={size/5} r={size/100*40} />
                </svg>
            );
        }
    }

}


