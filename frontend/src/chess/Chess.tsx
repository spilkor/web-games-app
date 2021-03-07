import React, {ChangeEvent, useContext, useState} from 'react';
import {AppContext} from "../App";
import {QuitButton, StartGameButton, SystemMessage} from "../Main Components/Game";
import API from "../util/API";
import {Coordinate, GameState} from "../util/types";

import {ReactComponent as KingSVG} from './king.svg';
import {ReactComponent as QueenSVG} from './queen.svg';
import {ReactComponent as RookSVG} from './rook.svg';
import {ReactComponent as BishopSVG} from './bishop.svg';
import {ReactComponent as KnightSVG} from './knight.svg';
import {ReactComponent as PawnSVG} from './pawn.svg';

import './chess.scss';
import {ChessGameDTO, ChessLobbyDTO, ChessMoveDTO, Color, OwnerAs, PieceType, SquireProps} from "./chessTypes";
import {ReactComponent as SurrenderFlag} from "../svg/surrender-flag.svg";
import {Modal} from "../Main Components/Modal";


export function Chess () {

    const { user, gameData } = useContext(AppContext);

    const chessGameDTO = JSON.parse(gameData!.gameJSON) as ChessGameDTO;
    const myMove = chessGameDTO && chessGameDTO.nextPlayer && chessGameDTO.nextPlayer.id === user!.id;

    const myColor = gameData!.owner.id === user!.id ? chessGameDTO.ownerColor : (chessGameDTO.ownerColor === Color.WHITE ? Color.BLACK : Color.WHITE) as Color;

    const [fromPosition, setFromPosition] = useState<Coordinate | null>(null);

    const [surrenderOpen, setSurrenderOpen] = useState<boolean>(false);

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
            case GameState.ENDED:
                return(
                    <ChessEnd/>
                );
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
            <div className={"game"}>
                <Table/>
                <SystemMessage text = {chessGameDTO.nextPlayer && chessGameDTO.nextPlayer.id === user!.id ? "Your move" : "Waiting for opponent"}/>
                {!surrenderOpen && gameData!.gameState === GameState.IN_GAME && <div className={"surrender-flag"} onClick={()=>setSurrenderOpen(true)}><SurrenderFlag/></div>}
                {surrenderOpen &&
                <Modal isOpen={surrenderOpen} closeOnBackGroundClick={true} close={()=> {setSurrenderOpen(false)}}>
                    <div className={"surrender-flag red"} onClick={()=> API.surrender()}><SurrenderFlag/></div>
                </Modal>
                }
            </div>
        );
    }

    function ChessEnd() {
        return (
            <div className={"end"}>
                <Table/>
                {
                    chessGameDTO.winner &&
                    <SystemMessage text = {chessGameDTO.winner.name + " won."}/>
                }
                {
                    chessGameDTO.draw &&
                    <SystemMessage text = {"Game ended in a draw."}/>
                }
                {
                    chessGameDTO.surrendered &&
                    <SystemMessage text = {chessGameDTO.surrendered.name + " gave up."}/>
                }
                {gameData!.owner.id === user!.id && <QuitButton/>}
            </div>
        );
    }


    function Table() {
        const isInverse = myColor === Color.BLACK;

        const rows = [];
        for (let x = 0; x < 8; x++) {
            const columns = [];
            for (let y = 0; y < 8; y++) {
                columns.push(
                    <td key={y}>
                        <Squire key={y + "_" + x} position={{x: isInverse ? 7-x : x, y: isInverse ? 7-y : y} as Coordinate}/>
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



    async function move(source: Coordinate, target: Coordinate) {
        let chessMoveDTO = {
            source: {
                column: source.y,
                row: source.x
            },
            target: {
                column: target.y,
                row: target.x
            }
        } as ChessMoveDTO;
        API.move(JSON.stringify(chessMoveDTO));
    }

    async function selectSquare(position: Coordinate) {
        if (fromPosition === null){
            let piece = chessGameDTO.table[position.x][position.y];
            if (piece !== null && piece.color === myColor){
                setFromPosition(position);
            }
        } else {
            let fp = fromPosition;
            setFromPosition(null);
            await move(fp, position);
        }
    }

    function Squire({position}: SquireProps) {
        const piece = chessGameDTO.table[position.x][position.y];
        const clickable = myMove;

        const isSelected = fromPosition && fromPosition.x === position.x && fromPosition.y === position.y;

        return(
            <div className={"square" + (isSelected ? " selected" : "") + (clickable ? " clickable" : "") + (piece === null ? "" : piece.color === Color.WHITE ? " white" : " black")} onClick={()=> clickable && selectSquare(position)}>
                {piece && <ChessPiece/>}
            </div>
        );


        function ChessPiece() {
            switch (piece.pieceType) {
                case PieceType.KING:
                    return(
                        <KingSVG/>
                    );
                case PieceType.QUEEN:
                    return(
                        <QueenSVG/>
                    );
                case PieceType.ROOK:
                    return(
                        <RookSVG/>
                    );
                case PieceType.BISHOP:
                    return(
                        <BishopSVG/>
                    );
                case PieceType.KNIGHT:
                    return(
                        <KnightSVG/>
                    );
                case PieceType.PAWN:
                    return(
                        <PawnSVG/>
                    );
            }

        }
    }

}


