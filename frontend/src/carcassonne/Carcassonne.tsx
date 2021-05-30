import React, {useContext, useState, WheelEvent} from 'react';


import {
    CarcassonneGameDTO,
    CarcassonneGameSettingsDTO,
    CarcassonneLobbyDTO,
    CarcassonneMoveDTO,
    Color,
    MoveType,
    Player,
    TileDTO
} from "./carcassonneTypes";
import {Coordinate, GameState, PointOfCompass} from "../util/types";
import Tile from "./Tile";
import './carcassonne.scss';
import {AppContext} from "../App";
import {QuitButton, StartGameButton, SystemMessage} from "../Main Components/Game";
import {ReactComponent as RotateSVG} from '../svg/rotate-left.svg';
import {ReactComponent as SurrenderFlag} from '../svg/surrender-flag.svg';
import logo from "./card-back-400X400.png";

import API from "../util/API";
import {Modal} from "../Main Components/Modal";

export function Carcassonne () {

    const { gameData, user, gameSettings, setGameSettings  } = useContext(AppContext);

    const defaultNextTilePointOfCompass = PointOfCompass.NORTH;
    const defaultIsRotating = false;
    const defaultOffset_X = 0;
    const defaultOffset_Y = 0;
    const defaultSize = 80;
    const maxSize = 150;
    const minSize = 70;

    const tempSetting = gameSettings as CarcassonneGameSettingsDTO;
    const carcassonneGameSettingsDTO = {
        nextTilePointOfCompass: tempSetting && tempSetting.nextTilePointOfCompass ? tempSetting.nextTilePointOfCompass : defaultNextTilePointOfCompass,
        isRotating: tempSetting && tempSetting.isRotating ? tempSetting.isRotating : defaultIsRotating,
        offset_X: tempSetting && tempSetting.offset_X ? tempSetting.offset_X : defaultOffset_X,
        offset_Y: tempSetting && tempSetting.offset_Y ? tempSetting.offset_Y : defaultOffset_Y,
        size: tempSetting && tempSetting.size ? tempSetting.size : defaultSize
    } as CarcassonneGameSettingsDTO;
    const {
        nextTilePointOfCompass,
        isRotating,
        offset_X,
        offset_Y,
        size
    } = carcassonneGameSettingsDTO;

    const carcassonneGameDTO = JSON.parse(gameData!.gameJSON) as CarcassonneGameDTO;
    const {players, finalPlayers} = carcassonneGameDTO;

    const [surrenderOpen, setSurrenderOpen] = useState<boolean>(false);

    function setSize(newSize: number) {
        setGameSettings!(
            {
                ...carcassonneGameSettingsDTO,
                size: Math.min(maxSize, Math.max(minSize, newSize))
            } as CarcassonneGameSettingsDTO
        );
    }

    function stopRotate(fromPointOfCompass: PointOfCompass) {
        switch (fromPointOfCompass) {
            case PointOfCompass.NORTH:
                setGameSettings!(
                    {
                        ...carcassonneGameSettingsDTO,
                        isRotating: false,
                        nextTilePointOfCompass: PointOfCompass.WEST
                    } as CarcassonneGameSettingsDTO
                ); return;
            case PointOfCompass.EAST:
                setGameSettings!(
                    {
                        ...carcassonneGameSettingsDTO,
                        isRotating: false,
                        nextTilePointOfCompass: PointOfCompass.NORTH
                    } as CarcassonneGameSettingsDTO
                ); return;
            case PointOfCompass.SOUTH:
                setGameSettings!(
                    {
                        ...carcassonneGameSettingsDTO,
                        isRotating: false,
                        nextTilePointOfCompass: PointOfCompass.EAST
                    } as CarcassonneGameSettingsDTO
                ); return;
            case PointOfCompass.WEST:
                setGameSettings!(
                    {
                        ...carcassonneGameSettingsDTO,
                        isRotating: false,
                        nextTilePointOfCompass: PointOfCompass.SOUTH
                    } as CarcassonneGameSettingsDTO
                ); return;
        }
    }

    function setNextTilePointOfCompass(pointOfCompass: PointOfCompass) {
        setTimeout(()=>{
            stopRotate(carcassonneGameSettingsDTO.nextTilePointOfCompass);
        }, 350);

        setGameSettings!(
            {
                ...carcassonneGameSettingsDTO,
                nextTilePointOfCompass: pointOfCompass,
                isRotating: true
            } as CarcassonneGameSettingsDTO
        );
    }

    function setOffset(x: number, y: number) {
        setGameSettings!(
            {
                ...carcassonneGameSettingsDTO,
                offset_X: x,
                offset_Y: y,
            } as CarcassonneGameSettingsDTO
        ); return;
    }

    const onMouseMove = (event: React.MouseEvent) => {
        if (event.buttons === 1){
            setOffset(offset_X + event.movementX, offset_Y + event.movementY);
        }
    };

    const onWheel = (event: WheelEvent) => {
        setSize(size - event.deltaY);
    };

    function Content () {
        switch (gameData!.gameState) {
            case GameState.IN_LOBBY:
                return(
                    <Lobby/>
                );
            case GameState.IN_GAME:
                return(
                    <Game/>
                );
            case GameState.ENDED:
                return(
                    <End/>
                );
            default:
                return null;
        }
    }

    function Lobby () {
        return (
            <div className={"lobby"}>
                <div className={"caption"}>
                    Carcassonne lobby
                </div>
                <div className={"row"}>
                    <div className={"key"}>
                        Owner:
                    </div>
                    <div className={"value"}>
                        <span>
                            {gameData!.owner.name}
                        </span>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"key"}>
                        Players:
                    </div>
                    <div className={"value"}>
                        {players.map((player, key)=>
                            <div key={key} className={"player " + player.color.toLowerCase()}>
                                <span >
                                    {player.user.name}
                                </span>
                                {gameData!.owner.id === user!.id &&
                                    <>
                                        <ColorPick color={Color.RED} userId={player.user.id}/>
                                        <ColorPick color={Color.BLUE} userId={player.user.id}/>
                                        <ColorPick color={Color.YELLOW} userId={player.user.id}/>
                                        <ColorPick color={Color.GREEN} userId={player.user.id}/>
                                    </>
                                }
                            </div>
                        )}
                    </div>
                </div>
                {gameData!.owner.id === user!.id &&
                <StartGameButton text={"START"} enabled={gameData!.startable === true}/>
                }
            </div>
        );

        function ColorPick({userId, color}: {userId: string, color: Color}) {
            return(
                <div className={"color " + color.toLowerCase()} onClick={()=>{setColor()}}>
                </div>
            );

            function setColor() {
                const carcassonneLobbyData = {
                    userId,
                    color
                } as CarcassonneLobbyDTO;

                API.sendLobbyData(JSON.stringify(carcassonneLobbyData));
            }
        }
    }

    function Board () {
        const { tiles, nextMoveType, tile, legalParts } = carcassonneGameDTO;

        const max_x = tiles.reduce(((currentValue, tile)=> {return Math.max(tile.coordinate.x, currentValue)}), 0);
        const min_x = tiles.reduce(((currentValue, tile)=> {return Math.min(tile.coordinate.x, currentValue)}), 0);
        const max_y = tiles.reduce(((currentValue, tile)=> {return Math.max(tile.coordinate.y, currentValue)}), 0);
        const min_y = tiles.reduce(((currentValue, tile)=> {return Math.min(tile.coordinate.y, currentValue)}), 0);

        const rows = [];
        for (let y = min_y-1; y <= max_y+1; y++) {
            const columns = [];
            for (let x = min_x-1; x <= max_x+1; x++) {
                const tile = getTile(x, y);
                const clickable = isPositionClickable({x,y} as Coordinate);
                columns.push(
                    <td key={x}>
                        <div className={"tile-wrapper" + (clickable ? " clickable" : "") + ((carcassonneGameDTO.tile && carcassonneGameDTO.tile.coordinate && carcassonneGameDTO.tile.coordinate.x === x && carcassonneGameDTO.tile.coordinate.y === y) ? " last-tile" : "")} onClick={()=>{clickable && placeTile({x, y} as Coordinate)}} style={{height: size, width: size}}>
                            {tile && <Tile legalParts={getLegalPartsForTile({x, y} as Coordinate)}  {...tile} size={size}/>}
                        </div>
                    </td>
                );
            }
            rows.push(
                <tr key={y}>
                    {columns}
                </tr>
            )
        }

        function getLegalPartsForTile(coordinate: Coordinate): number[] | null {
            if (nextMoveType === MoveType.MEEPLE && tile!.coordinate.x === coordinate.x && tile!.coordinate.y === coordinate.y){
                return legalParts;
            } else {
                return null;
            }
        }

        function placeTile(coordinate: Coordinate) {
            let carcassonneMoveDTO = {
                coordinate,
                pointOfCompass: nextTilePointOfCompass,
                skip: false
            } as CarcassonneMoveDTO;
            API.move(JSON.stringify(carcassonneMoveDTO));
        }

        function isPositionClickable(coordinate: Coordinate){
            const { playableTilePositions } = carcassonneGameDTO;
            return playableTilePositions && playableTilePositions.some((playableTilePosition) => {
                return playableTilePosition.coordinate.x === coordinate.x
                    && playableTilePosition.coordinate.y === coordinate.y
                    && playableTilePosition.pointOfCompass === nextTilePointOfCompass
            });
        }

        function getTile(x: number, y: number): TileDTO | undefined{
            return tiles.find(tile => tile.coordinate.x === x && tile.coordinate.y === y);
        }

        return(
            <div className={"board"} style={{translate: offset_X + "px " + offset_Y + "px"}}>
                <div className={"table"}>
                    <table>
                        <tbody>
                        {rows}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }

    function PlayerBoard () {
        return (
            <div className={"player-board"}>
                <table>
                    <tbody>
                        <tr>
                            <td className={"border-none"}>
                            </td>
                            <td>
                                M
                            </td>
                            <td>
                                VP
                            </td>
                        </tr>
                    {GameState.ENDED === gameData!.gameState ?
                        finalPlayers.map((player, key)=>
                            <PlayerRow key = {key} player={player}/>
                        )
                        :
                        players.map((player, key)=>
                            <PlayerRow key = {key} player={player}/>
                        )
                    }
                    </tbody>
                </table>
            </div>
        );
    }

    type PlayerRowProps = {
        player: Player
    }

    function PlayerRow ({player}: PlayerRowProps) {
        const isNextPlayer = carcassonneGameDTO.nextPlayer && carcassonneGameDTO.nextPlayer.user.id === player.user.id === true;
        return(
            <tr className={
                "player-row " +
                player.color.toLowerCase() +
                (player.isWinner ? " winner" : (isNextPlayer ? " next" : ""))
            }>
                <td>
                    <div className={"player-name"}>
                        {player.user.name}
                    </div>
                </td>
                <td>
                    <div className={"player-meeples"}>
                        {player.meeples}
                    </div>
                </td>
                <td>
                    <div className={"player-victory-points"}>
                        {player.victoryPoints}
                    </div>
                </td>
            </tr>
        );
    }

    function Action () {

        return (
            <div className={"action"}>
                {carcassonneGameDTO.tile && carcassonneGameDTO.nextMoveType === MoveType.TILE &&
                <div className={"next-tile"}>
                    <div className={"tile-wrapper " + nextTilePointOfCompass.toLowerCase() + (isRotating ? " rotating" : "")} style={{height: 100, width: 100}}>
                        <Tile {...carcassonneGameDTO.tile!} size={100}  />
                    </div>
                    {
                        carcassonneGameDTO.nextPlayer && carcassonneGameDTO.nextPlayer.user.id === user!.id &&
                        <div className={"rotate-icon" + (isRotating ? " rotating" : "")} onClick={()=>rotateLeft()} >
                            <RotateSVG/>
                        </div>
                    }
                </div>
                }

                {carcassonneGameDTO && carcassonneGameDTO.nextMoveType === MoveType.MEEPLE && carcassonneGameDTO.nextPlayer &&
                <div className={"next-meeple"}>
                    <svg className={"meeple " + carcassonneGameDTO.nextPlayer.color.toLowerCase()}>
                        <path d="M 0.000 -15.704 c -2.109 0.000 -3.626 1.116 -4.503 2.518 -0.784 1.254 -1.110 2.708 -1.173 3.981 -2.391 1.182 -5.040 2.375 -7.173 3.593 -1.119 0.639 -2.094 1.282 -2.828 1.973 C -16.411 -2.947 -16.953 -2.169 -16.953 -1.251 c 0.000 0.391 0.191 0.709 0.405 0.943 0.215 0.234 0.473 0.415 0.767 0.584 0.587 0.339 1.327 0.621 2.142 0.865 1.210 0.363 2.564 0.633 3.742 0.748 -1.172 2.019 -2.731 3.873 -4.097 5.650 C -15.574 9.593 -16.953 11.562 -16.953 13.749 c 0.000 0.313 -0.003 0.556 0.024 0.802 0.028 0.245 0.098 0.551 0.345 0.793 0.246 0.243 0.548 0.308 0.794 0.334 0.246 0.027 0.493 0.024 0.811 0.024 h 8.714 c 0.633 0.000 1.096 0.038 1.572 -0.268 0.477 -0.306 0.650 -0.725 0.990 -1.350 l 0.007 -0.013 0.006 -0.013 s 0.760 -1.545 1.666 -3.077 c 0.453 -0.766 0.945 -1.529 1.373 -2.070 0.214 -0.270 0.414 -0.485 0.559 -0.606 0.044 -0.037 0.064 -0.044 0.093 -0.059 0.028 0.016 0.048 0.023 0.093 0.059 0.144 0.121 0.345 0.335 0.559 0.606 0.428 0.541 0.920 1.304 1.373 2.070 0.906 1.533 1.666 3.077 1.666 3.077 l 0.006 0.013 0.007 0.013 c 0.340 0.625 0.512 1.041 0.985 1.348 0.473 0.307 0.935 0.270 1.559 0.270 H 15.000 c 0.313 0.000 0.556 0.003 0.800 -0.024 0.244 -0.027 0.547 -0.095 0.791 -0.338 0.244 -0.244 0.311 -0.547 0.338 -0.791 0.027 -0.244 0.024 -0.488 0.024 -0.800 0.000 -2.188 -1.379 -4.157 -2.958 -6.210 -1.367 -1.777 -2.926 -3.631 -4.097 -5.650 1.178 -0.115 2.533 -0.385 3.742 -0.748 0.814 -0.244 1.554 -0.527 2.142 -0.865 0.294 -0.169 0.551 -0.351 0.766 -0.584 0.215 -0.234 0.406 -0.552 0.406 -0.943 0.000 -0.918 -0.542 -1.696 -1.276 -2.387 -0.734 -0.691 -1.710 -1.334 -2.828 -1.973 -2.133 -1.219 -4.782 -2.411 -7.173 -3.593 -0.064 -1.273 -0.390 -2.728 -1.173 -3.981 C 3.626 -14.588 2.109 -15.704 0.000 -15.704 z"/>
                        {carcassonneGameDTO.nextPlayer.meeples === 0 &&
                        <>
                            <line x1={-18} y1={-16} x2={18} y2={20} strokeWidth={2} strokeLinecap="round" />
                            <line x1={18} y1={-16} x2={-18} y2={20} strokeWidth={2} strokeLinecap="round" />
                        </>}
                    </svg>
                    {
                        carcassonneGameDTO.nextPlayer && carcassonneGameDTO.nextPlayer.user.id === user!.id &&
                        <div className={"skip"} onClick={()=> skip()}>
                            Skip
                        </div>
                    }
                </div>
                }
            </div>
        );

        function skip() {
            let carcassonneMoveDTO = {
                skip: true
            } as CarcassonneMoveDTO;
            API.move(JSON.stringify(carcassonneMoveDTO));
        }

        function rotateLeft() {
            if (isRotating){
                return;
            }
            switch (nextTilePointOfCompass) {
                case PointOfCompass.NORTH:
                    setNextTilePointOfCompass(PointOfCompass.WEST);
                    return;
                case PointOfCompass.EAST:
                    setNextTilePointOfCompass(PointOfCompass.NORTH);
                    return;
                case PointOfCompass.SOUTH:
                    setNextTilePointOfCompass(PointOfCompass.EAST);
                    return;
                case PointOfCompass.WEST:
                    setNextTilePointOfCompass(PointOfCompass.SOUTH);
                    return;
            }
        }
    }

    function Game () {
        return(
            <div className={"game"}>
                <Deck/>
                <Board/>
                {gameData!.gameState === GameState.IN_GAME && <Action/>}
                <PlayerBoard/>
                {!surrenderOpen && gameData!.gameState === GameState.IN_GAME && <div className={"surrender-flag"} onClick={()=>setSurrenderOpen(true)}><SurrenderFlag/></div>}
                {surrenderOpen &&
                <Modal isOpen={surrenderOpen} closeOnBackGroundClick={true} close={()=> {setSurrenderOpen(false)}}>
                    <div className={"surrender-flag red"} onClick={()=> API.surrender()}><SurrenderFlag/></div>
                </Modal>
                }
            </div>
        );
    }

    function Deck () {

        const {deckSize} = carcassonneGameDTO;

        return(
            <div className={"deck"}>
                {deckSize > 0 && <div className={"deck-size"} >{deckSize}</div>}
                {deckSize > 4 && <img src={logo} />}
                {deckSize > 3 && <img src={logo} />}
                {deckSize > 2 && <img src={logo} />}
                {deckSize > 1 && <img src={logo} />}
                {deckSize > 0 && <img src={logo} />}
            </div>
        );
    }

    function End () {
        return (
                <>
                    <Game/>
                    {gameData!.owner.id === user!.id && <QuitButton/>}
                    {carcassonneGameDTO.surrendered ?
                        <div className={"system-message-wrapper"}>
                            <SystemMessage text = {carcassonneGameDTO.surrendered.user.name + " gave up."}/>
                        </div>
                        :
                        <div className={"system-message-wrapper"}>
                            <SystemMessage text = {"" + carcassonneGameDTO.finalPlayers.filter(fp => fp.isWinner).map(winner => {return winner.user.name}).reduce(((result, name)=> {return result + (result.length === 0 ? "" : ", ") + name}), "") + " won."}/>
                        </div>
                    }
                </>
            );
    }

    return(
        <div className={"carcassonne"} onMouseMove={(event)=>{onMouseMove(event)}} onWheel={(event)=>{gameData!.gameState !== GameState.IN_LOBBY && onWheel(event)}}>
            <Content />
        </div>
    );

}


