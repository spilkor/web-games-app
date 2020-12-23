import React, {ChangeEvent, useContext} from 'react';
import {AppContext, GameData, GameType} from "../../App";
import {GameButton, StartGameButton} from "../Game";
import API from "../../util/API";
import {GameState, User} from "../../util/types";

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

type AmobaLobbyDTO = {
    ownerAs: OwnerAs
}

type AmobaEndDTO = {
    winner: User
}

type AmobaGameDTO = {
    nextPlayer: User | null,
    table: Boolean[]
}


type AmobaMoveDTO = {
    index: number
}

export function Amoba () {


    const { user, gameData, setUsersOpen} = useContext(AppContext);

    const amobaGameData = JSON.parse(gameData!.gameJSON) as AmobaGameDTO;
    const amobaLobbyData = JSON.parse(gameData!.lobbyJSON) as AmobaLobbyDTO;
    const amobaEndData = JSON.parse(gameData!.endJSON) as AmobaEndDTO;

    const myMove = amobaGameData && amobaGameData.nextPlayer && amobaGameData.nextPlayer.id === user!.id;

    switch (gameData!.gameState) {
        case GameState.IN_LOBBY:
            return(
                    <AmobaLobby/>
                );
        case GameState.IN_GAME:
        case GameState.GAME_END:
            return(
                <Table/>
            );
        default:
            return null;
    }

    function AmobaLobby () {
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
                        <span>{gameData!.owner.name}</span>
                    </div>

                    <div className={"key"}>
                        as:
                    </div>
                    <div className={"value"}>
                        <select disabled={! (user!.id == gameData!.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setOwnerAs(e.target.value as OwnerAs)}} value={amobaLobbyData.ownerAs}>
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
                            <div><span>{user.name}</span></div>
                        )}
                    </div>
                </div>

                {
                    gameData!.owner.id === user!.id && <StartGameButton text={"START"} enabled={user!.id === gameData!.owner.id && gameData!.startable === true}/>
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


    function Table() {
        return (
            <div className={"amoba"}>
                <div >
                    <Squire index={0}/>
                    <Squire index={1}/>
                    <Squire index={2}/>
                </div>
                <div >
                    <Squire index={3}/>
                    <Squire index={4}/>
                    <Squire index={5}/>
                </div>
                <div >
                    <Squire index={6}/>
                    <Squire index={7}/>
                    <Squire index={8}/>
                </div>

                <div className={"system-message"}>
                    {amobaGameData.nextPlayer === null ? (amobaEndData.winner.id === user!.id ? "You won" : "You lost") : (myMove ? "Your move" : "Waiting for the opponent")}
                </div>
            </div>
        );
    }


    type SquireProps = {
        index: number
    }

    function Squire({index}: SquireProps) {
        const { user, gameData} = useContext(AppContext);
        const value = amobaGameData.table[index];
        const clickable = amobaGameData && amobaGameData.nextPlayer && amobaGameData.nextPlayer.id === user!.id && value === null && gameData && gameData.gameState === GameState.IN_GAME;

        return(
          <div className={"squire" + (clickable ? " clickable" : "")} onClick={()=> myMove && clickable && move()}>
              {value === true && <Squire_X/>}
              {value === false && <Squire_O/>}
          </div>
        );

        function move() {
            let amobaMoveDTO = { index } as AmobaMoveDTO;
            API.move(JSON.stringify(amobaMoveDTO));
        }

        function Squire_X() {
            const size = 50;
            return(
                <svg height={size} width={size} className={"amoba_x"}>
                    <line x1={size/10}  y1={size/10}  x2={size/10*9}  y2={size/10*9} strokeWidth={size/5} stroke-linecap="round" />
                    <line x1={size/10} y1={size/10*9} x2={size/10*9} y2={size/10} strokeWidth={size/5} stroke-linecap="round" />
                </svg>
            );
        }

        function Squire_O() {
            const size = 50;
            return(
                <svg className="amoba_o">
                    <circle cx={size/2} cy={size/2} stroke-width={size/5} r={size/100*40} />
                </svg>
            );
        }
    }

}


