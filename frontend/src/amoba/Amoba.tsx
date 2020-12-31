import React, {ChangeEvent, useContext} from 'react';
import {AppContext} from "../App";
import {Position, QuitButton, StartGameButton, SystemMessage} from "../components/Game";
import API from "../util/API";
import {GameState} from "../util/types";

import {ReactComponent as ZoomInSVG} from '../svg/zoom-in.svg';
import {ReactComponent as ZoomOutSVG} from '../svg/zoom-out.svg';

import {ReactComponent as LeftSVG} from '../svg/left.svg';
import {ReactComponent as RightSVG} from '../svg/right.svg';
import {ReactComponent as UpSVG} from '../svg/up.svg';
import {ReactComponent as DownSVG} from '../svg/down.svg';

import {ReactComponent as ResetSVG} from '../svg/reset.svg';

import './amoba.scss';
import {
    AmobaGameDTO,
    AmobaGameSettings,
    AmobaLobbyDTO,
    AmobaMoveDTO,
    AmobaSize,
    AmobaSizes,
    OwnerAs,
    SquireProps
} from "./amobaTypes";


export function Amoba () {

    const { user, gameData, gameSettings, setGameSettings } = useContext(AppContext);

    const amobaGameDTO = JSON.parse(gameData!.gameJSON) as AmobaGameDTO;
    const myMove = amobaGameDTO && amobaGameDTO.nextPlayer && amobaGameDTO.nextPlayer.id === user!.id;

    const defaultNum_x = 11;
    const defaultMiddle_x = 100;
    const defaultMiddle_y = 100;

    const tempSetting = gameSettings as AmobaGameSettings;
    const amobaGameSettings = {
        num_x: tempSetting && tempSetting.num_x ? tempSetting.num_x : defaultNum_x,
        middle_x: tempSetting && tempSetting.middle_x ? tempSetting.middle_x : defaultMiddle_x,
        middle_y: tempSetting && tempSetting.middle_y ? tempSetting.middle_y : defaultMiddle_y
    } as AmobaGameSettings;

    const { num_x , middle_x, middle_y} = amobaGameSettings;

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
                <div className={"caption"}>Amoba lobby</div>
                <div className={"row"}>
                    <div className={"key"}>
                        Size:
                    </div>
                    <div className={"value"}>
                        <select disabled={! (user!.id == gameData!.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {setAmobaSize(e.target.value)}} value={amobaGameDTO.amobaSize}>
                            <option value = {AmobaSizes.three.id}>{AmobaSizes.three.name}</option>
                            <option value = {AmobaSizes.twoHundred.id}>{AmobaSizes.twoHundred.name}</option>
                        </select>
                    </div>
                </div>
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

        function setAmobaSize(amobaSize: string) {
            const amobaLobbyData = {
                amobaSize: amobaSize
            } as AmobaLobbyDTO;

            API.sendLobbyData(JSON.stringify(amobaLobbyData));
        }

        function setOwnerAs(ownerAs: OwnerAs) {
            const amobaLobbyData = {
                ownerAs: ownerAs
            } as AmobaLobbyDTO;

            API.sendLobbyData(JSON.stringify(amobaLobbyData));
        }

    }

    function AmobaGame() {
        const nextSign = amobaGameDTO.nextSign ? "X" : "O";

        switch (amobaGameDTO.amobaSize) {
            case AmobaSize.three:
                return (
                    <div>
                        <ThreeTable/>
                        <SystemMessage text = {amobaGameDTO.nextPlayer && amobaGameDTO.nextPlayer.id === user!.id ? ("Your move (" + nextSign + ")") : ("Waiting for opponent (" + nextSign + ")")}/>
                    </div>
                );
            case AmobaSize.twoHundred:
                return (
                    <div>
                        <TwoHundredTable/>
                        <SystemMessage text = {amobaGameDTO.nextPlayer && amobaGameDTO.nextPlayer.id === user!.id ? ("Your move (" + nextSign + ")") : ("Waiting for opponent (" + nextSign + ")")}/>
                    </div>
                );
            default:
                return null;
        }
    }

    function AmobaEnd () {
        return (
            <div className={"end"}>
                {amobaGameDTO.amobaSize === AmobaSize.three ? <ThreeTable/> : <TwoHundredTable/>}
                <SystemMessage text = {!amobaGameDTO.winner ? "Game ended in a draw" : amobaGameDTO.winner.id === user!.id ? "You won" : "You lost"}/>
                {gameData!.owner.id === user!.id && <QuitButton/>}
            </div>
        );
    }

    function ThreeTable() {
        //    2|2 2|1 2|0
        //    1|2 1|1 1|0
        //    0|2 0|1 0|0

        return (
            <table>
                <tbody>
                    <tr>
                        <td>
                            <Squire position={{x:2, y: 2} as Position} size={50}/>
                        </td>
                        <td>
                            <Squire position={{x:2, y: 1} as Position} size={50}/>
                        </td>
                        <td>
                            <Squire position={{x:2, y: 0} as Position} size={50}/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <Squire position={{x:1, y: 2} as Position} size={50}/>
                        </td>
                        <td>
                            <Squire position={{x:1, y: 1} as Position} size={50}/>
                        </td>
                        <td>
                            <Squire position={{x:1, y: 0} as Position} size={50}/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <Squire position={{x:0, y: 2} as Position} size={50}/>
                        </td>
                        <td>
                            <Squire position={{x:0, y: 1} as Position} size={50}/>
                        </td>
                        <td>
                            <Squire position={{x:0, y: 0} as Position} size={50}/>
                        </td>
                    </tr>
                </tbody>
            </table>
        );
    }

    function TwoHundredTable() {
        const height = 550;

        const size = height / num_x;
        const actualSize = size - 2;

        const rows = [];
        for (let x = 0; x < num_x; x++) {
            const index_x = middle_x + ((num_x -1) / 2) - x;
            const columns = [];
            for (let y = 0; y < num_x * 2 - 1; y++) {
                const index_y = middle_y + num_x - 1 - y;
                columns.push(
                    <td className={(index_x == 199 ? "strong-top" : "") + (index_x == 0 ? " strong-bottom" : "") + (index_y == 199 ? " strong-left" : "") + (index_y == 0 ? " strong-right" : "")} key={y}>
                        <Squire key={y + "_" + x} position={{x: index_x, y: index_y} as Position} size={actualSize}/>
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
            <div className={"infinite"}>
                <div className={"zoom-in-logo"} onClick={()=> zoomIn()}>
                    <ZoomInSVG/>
                </div>
                <div className={"zoom-out-logo"} onClick={()=> zoomOut()}>
                    <ZoomOutSVG/>
                </div>
                <div className={"up-logo"} onClick={()=> up()}>
                    <UpSVG/>
                </div>
                <div className={"down-logo"} onClick={()=> down()}>
                    <DownSVG/>
                </div>
                <div className={"left-logo"} onClick={()=> left()}>
                    <LeftSVG/>
                </div>
                <div className={"right-logo"} onClick={()=> right()}>
                    <RightSVG/>
                </div>
                <div className={"reset-logo"} onClick={()=> reset()}>
                    <ResetSVG/>
                </div>
                <table>
                    <tbody>
                        {rows}
                    </tbody>
                </table>
            </div>
        );

        function reset() {
            setGameSettings!(
                {
                    num_x: defaultNum_x,
                    middle_x: defaultMiddle_x,
                    middle_y: defaultMiddle_y
                } as AmobaGameSettings
            );
        }

        function down() {
            if (middle_x - ((num_x - 1) / 2) > 0){
                setGameSettings!(
                    {
                        num_x,
                        middle_x: middle_x - 1,
                        middle_y
                    } as AmobaGameSettings
                );
            }
        }

        function up() {
            if (middle_x + ((num_x - 1) / 2) < 199){
                setGameSettings!(
                    {
                        num_x,
                        middle_x: middle_x + 1,
                        middle_y
                    } as AmobaGameSettings
                );
            }
        }

        function right() {
            if (middle_y - (num_x - 1) > 0){
                setGameSettings!(
                    {
                        num_x,
                        middle_x,
                        middle_y: middle_y - 1
                    } as AmobaGameSettings
                );
            }
        }

        function left() {
            if (middle_y + (num_x - 1) < 199){
                setGameSettings!(
                    {
                        num_x,
                        middle_x,
                        middle_y: middle_y + 1
                    } as AmobaGameSettings
                );
            }
        }

        function zoomIn() {
            if (num_x > 3){
                setGameSettings!(
                    {
                        num_x: num_x - 2,
                        middle_x: middle_x,
                        middle_y: middle_y
                    } as AmobaGameSettings
                );
            }
        }

        function zoomOut() {
            if (num_x < 19){
                setGameSettings!(
                    {
                        num_x: num_x + 2,
                        middle_x: (middle_x + ((num_x + 2) -1 ) / 2 > 199) ? (middle_x - 1) : (middle_x - ((num_x + 2) -1 ) / 2 < 0) ? (middle_x + 1) : middle_x,
                        middle_y: (middle_y + ((num_x + 2) -1) > 199) ? (199 - (num_x + 2 - 1)) : (middle_y - ((num_x + 2) -1 ) < 0) ? (num_x + 2 - 1) : middle_y
                    } as AmobaGameSettings
                );
            }
        }

    }

    function Squire({position, size}: SquireProps) {
        const value = amobaGameDTO.table[position.x][position.y];

        const clickable = myMove && value === null;
        const isLast = amobaGameDTO.lastPosition && amobaGameDTO.lastPosition.x === position.x && amobaGameDTO.lastPosition.y === position.y;

        return(
            <div style={{height: size, width: size}} className={"square" + (clickable ? " clickable" : "") + (isLast ? " last" : "")} onClick={()=> clickable && move(position)}>
                {value === true && <Squire_X />}
                {value === false && <Squire_O/>}
            </div>
        );

        function move(position: Position) {
            let amobaMoveDTO = { position: position } as AmobaMoveDTO;
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
                <svg height={size} width={size}>
                    <circle cx={size/2} cy={size/2} strokeWidth={size/5} r={size/100*40} />
                </svg>
            );
        }
    }

}


