import React, {useContext, useState} from 'react';
import {Amoba} from "./amoba/Amoba";
import {GameButtonProps, StartGameButtonProps} from "../util/types";
import {AppContext } from "../App";
import API from "../util/API";

import {ReactComponent as QuitLogo} from '../svg/quit.svg';
import {Chess} from "./chess/Chess";
import {Carcassonne} from "./carcassonne/Carcassonne";
import {Snapszer} from "./snapszer/Snapszer";

export enum GameType  {
    AMOBA = "AMOBA",
    CHESS = "CHESS",
    CARCASSONNE = "CARCASSONNE",
    SNAPSZER = "SNAPSZER"
}

export function Game () {

    const { gameData, gameTypes } = useContext(AppContext);

    if (gameData === undefined){
        return null;
    }

    if (gameData === null){
        return(
            <LocalLobby/>
        );
    }

    switch (gameData.gameType) {
        case GameType.AMOBA:
            return(
                <Amoba/>
            );
        case GameType.CHESS:
            return(
                <Chess/>
            );
        case GameType.CARCASSONNE:
            return(
                <Carcassonne/>
            );
        case GameType.SNAPSZER:
            return(
                <Snapszer/>
            );

        default: return null;
    }

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
                        {/*<GameButton enabled={true} text={"JOIN LOBBY"} onClick={()=> {setState(State.CHOOSE_LOBBY_TO_JOIN)}}/>*/}
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
                        <GameButton enabled={true} key={key} text={gameType} onClick={()=> {createGame(gameType)}}/>
                    )}
                </div>
            );

            function createGame(gameType: GameType) {
                API.createGame(gameType);
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

export function StartGameButton({text, enabled}: StartGameButtonProps) {
    return (
        <div className={"start-button game-button" + (enabled ? "" : " disabled")} onClick={()=> {enabled && API.startGame()}}>
            {text}
        </div>
    );
}


type SystemMessageProps = {
    text : string
}

export function SystemMessage({text}:SystemMessageProps) {

    return(
        <div className={"system-message"}>
            {text}
        </div>
    );

}

type QuitButtonProps = {

}

export function QuitButton({}:QuitButtonProps) {

    return(
        <div className={"quit-logo"} onClick={()=> API.restartGame()}>
            <QuitLogo/>
        </div>
    );

}

