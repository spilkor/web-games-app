import React, {ChangeEvent, useContext, useState} from 'react';
import {AppContext} from "../App";
import {Position, StartGameButton, SystemMessage} from "../components/Game";
import API from "../util/API";
import {GameState} from "../util/types";

import {ReactComponent as KingSVG} from './king.svg';
import {ReactComponent as QueenSVG} from './queen.svg';
import {ReactComponent as RookSVG} from './rook.svg';
import {ReactComponent as BishopSVG} from './bishop.svg';
import {ReactComponent as KnightSVG} from './knight.svg';
import {ReactComponent as PawnSVG} from './pawn.svg';

import './chess.scss';
import {
    ChessGameDTO,
    ChessLobbyDTO,
    ChessMoveDTO,
    ChessPiece,
    ChessPieceType,
    Color,
    OwnerAs,
    SquireProps
} from "./chessTypes";


export function Chess () {

    const { user, gameData } = useContext(AppContext);

    const chessGameDTO = JSON.parse(gameData!.gameJSON) as ChessGameDTO;
    // const myMove = chessGameDTO && chessGameDTO.nextPlayer && chessGameDTO.nextPlayer.id === user!.id;

    const [fromPosition, setFromPosition] = useState<Position | null>(null);

    return(
        <div className={"chess"}>
            <Content/>
        </div>
    );

    function Content () {
        switch (gameData!.gameState) {
            case GameState.IN_LOBBY:
                return(
                    <ChessLobby/>
                );
            case GameState.IN_GAME:
                return(
                    <ChessGame/>
                );
                // TODO
            // case GameState.ENDED:
            //     return(
            //         <ChessEnd/>
            //     );
            default:
                return null;
        }
    }

    function ChessLobby () {
        return (
            <div className={"lobby"}>
                <div className={"caption"}>Chess lobby</div>
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
                        <select disabled={! (user!.id == gameData!.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setOwnerAs(e.target.value as OwnerAs)}} value={chessGameDTO.ownerAs}>
                            <option value = {OwnerAs.Random}>{OwnerAs.Random}</option>
                            <option value = {OwnerAs.WHITE}>{OwnerAs.WHITE}</option>
                            <option value = {OwnerAs.BLACK}>{OwnerAs.BLACK}</option>
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
            const chessLobbyData = {
                ownerAs: ownerAs
            } as ChessLobbyDTO;

            API.sendLobbyData(JSON.stringify(chessLobbyData));
        }

    }

    function ChessGame() {
        return (
            <div className={"relative"}>
                <Table/>
                <SystemMessage text = {chessGameDTO.nextPlayer && chessGameDTO.nextPlayer.id === user!.id ? "Your move" : "Waiting for opponent"}/>
            </div>
        );
    }


    function Table() {
        const isInverse = chessGameDTO.startingPlayer.id !== user!.id;

        const rows = [];
        for (let x = 0; x < 8; x++) {
            const columns = [];
            for (let y = 0; y < 8; y++) {
                columns.push(
                    <td key={y}>
                        <Squire key={y + "_" + x} position={{x: isInverse ? 7-x : x, y: isInverse ? 7-y : y} as Position}/>
                    </td>
                );
            }
            rows.push(
                <tr key={x}>
                    {columns}
                </tr>
            )
        }

        return (
            <table>
                <tbody>
                    {rows}
                </tbody>
            </table>
        );
    }



    async function move(fromPosition: Position, toPosition: Position) {
        let chessMoveDTO = {
            fromPosition,
            toPosition
        } as ChessMoveDTO;
        API.move(JSON.stringify(chessMoveDTO));
    }

    async function selectSquare(position: Position) {
        if (fromPosition === null){
            setFromPosition(position);
        } else {
            let fp = fromPosition;
            setFromPosition(null);
            await move(fp, position);
        }
    }

    function Squire({position}: SquireProps) {
        const piece = chessGameDTO.table[position.x][position.y];
        const clickable = true; //FIXME

        const isSelected = fromPosition && fromPosition.x === position.x && fromPosition.y === position.y;

        return(
            <div className={"square" + (isSelected ? " selected" : "") + (piece === null ? "" : piece.color === Color.WHITE ? " white" : " black")} onClick={()=> clickable && selectSquare(position)}>
                {piece&& <ChessPiece/>}
            </div>
        );


        function ChessPiece() {
            switch (piece.type) {
                case ChessPieceType.KING:
                    return(
                        <KingSVG/>
                    );
                case ChessPieceType.QUEEN:
                    return(
                        <QueenSVG/>
                    );
                case ChessPieceType.ROOK:
                    return(
                        <RookSVG/>
                    );
                case ChessPieceType.BISHOP:
                    return(
                        <BishopSVG/>
                    );
                case ChessPieceType.KNIGHT:
                    return(
                        <KnightSVG/>
                    );
                case ChessPieceType.PAWN:
                    return(
                        <PawnSVG/>
                    );
            }

        }
    }

}


