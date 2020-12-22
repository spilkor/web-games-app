import React, {useContext, useState} from 'react';
import {Amoba, AmobaLobby} from "./game/Amoba";
import {GameButtonProps, GameState, StatGameButtonProps} from "../util/types";
import {AppContext, GameType} from "../App";
import {Simulate} from "react-dom/test-utils";
import API from "../util/API";

export function Game () {

    const { gameData, gameTypes } = useContext(AppContext);

    if (!gameData){
        return(
            <LocalLobby/>
        );
    }

    switch (gameData.gameState) {
        case GameState.IN_LOBBY:
            switch (gameData.gameType) {
                case GameType.AMOBA:
                    return(
                        <AmobaLobby {...gameData}/>
                    );
                // case GameType.CHESS:
                    // return(
                    //     <ChessLobby/>
                    // );
            }

        case GameState.IN_GAME:
            switch (gameData.gameType) {
                case GameType.AMOBA:
                    return(
                        <Amoba {...JSON.parse(gameData.gameJSON)}/>
                    );
                // case GameType.CHESS:
                //     return(
                //         <Chess {...JSON.parse(gameData.gameJSON)}/>
                //     );
            }
    }

    return null;

    function LocalLobby() {

        enum State {
            INITIAL,
            CHOSE_GAME_TYPE,
            CHOOSE_LOBBY_TO_JOIN
        }

        const [state, setState] = useState<State>(State.INITIAL);

        switch (state) {
            case State.INITIAL:
                return (
                    <div>
                        <GameButton enabled={true} text={"NEW LOBBY"} onClick={()=> {setState(State.CHOSE_GAME_TYPE)}}/>
                        <GameButton enabled={true} text={"JOIN LOBBY"} onClick={()=> {setState(State.CHOOSE_LOBBY_TO_JOIN)}}/>
                    </div>
                );
            case State.CHOSE_GAME_TYPE:
                return(
                    <ChoseGameType/>
                );
            case State.CHOOSE_LOBBY_TO_JOIN:
                return(
                    <ChoseLobby/>
                );
        }

        function ChoseGameType() {
            return (
                <div>
                    {gameTypes!.map((gameType, key)=>
                        <GameButton enabled={true} key={key} text={gameType} onClick={()=> {createGroup(gameType)}}/>
                    )}
                </div>
            );

            function createGroup(gameType: GameType) {
                // TODO

                API.createGroup(gameType);
            }
        }

        function ChoseLobby() {
            return (
                <div>
                    CHOOSE LOBBY
                </div>
            );
        }
    }



}

export function GameButton({text, onClick, enabled}: GameButtonProps) {
    return (
        <div className={"game-button" + (enabled ? "" : " disabled")} onClick={()=> {if(enabled) onClick()}}>
            {text}
        </div>
    );
}