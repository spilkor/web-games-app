import React, {useContext, useState} from 'react';


import {
    CarcassonneGameDTO,
    CarcassonneLobbyDTO,
    CarcassonneMoveDTO,
    Color,
    MoveType,
    TileDTO
} from "./carcassonneTypes";
import {Coordinate, GameState, PointOfCompass} from "../util/types";
import Tile, {size} from "./Tile";
import './carcassonne.scss';
import {AppContext} from "../App";
import {StartGameButton} from "../Main Components/Game";
import {ReactComponent as RotateSVG} from '../svg/rotate-left.svg';
import API from "../util/API";
import {AmobaLobbyDTO} from "../amoba/amobaTypes";

export function Carcassonne () {

    const { gameData, user } = useContext(AppContext);

    const carcassonneGameDTO = JSON.parse(gameData!.gameJSON) as CarcassonneGameDTO;

    const players = carcassonneGameDTO.players;


    const [pointOfCompass, setPointOfCompass] = useState<PointOfCompass>(PointOfCompass.NORTH);

    console.log("carcassonneGameDTO: ", carcassonneGameDTO);

    const [offset_X, setOffset_X] = useState<number>(0);
    const [offset_Y, setOffset_Y] = useState<number>(0);

    const onMouseMove = (event: React.MouseEvent) => {
        if (event.buttons === 1){
            console.log(event.buttons);
            setOffset_X(offset_X + event.movementX);
            setOffset_Y(offset_Y + event.movementY);
        }
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

                {/*<div className={"row"}>*/}
                    {/*<div className={"key"}>*/}
                        {/*Size:*/}
                    {/*</div>*/}
                    {/*<div className={"value"}>*/}
                        {/*<select disabled={! (user!.id == gameData!.owner.id)} onChange={(e:ChangeEvent<HTMLSelectElement>)=> {}} value={"VALUE"}>*/}
                            {/*<option value = {AmobaSizes.three.id}>{AmobaSizes.three.name}</option>*/}
                            {/*<option value = {AmobaSizes.twoHundred.id}>{AmobaSizes.twoHundred.name}</option>*/}
                        {/*</select>*/}
                    {/*</div>*/}
                {/*</div>*/}

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


        // const tiles = [
        //     {
        //         id: TileID.TILE_0,
        //         coordinate: {x: 0, y: 0} as Coordinate,
        //         legalParts: [0, 1],
        //         pointOfCompass: PointOfCompass.NORTH
        //     } as TileDTO,
        //     {
        //         id: TileID.TILE_1,
        //         coordinate: {x: 0, y: 1} as Coordinate,
        //         legalParts: [0, 1],
        //         pointOfCompass: PointOfCompass.NORTH
        //     } as TileDTO,
        //     {
        //         id: TileID.TILE_2,
        //         coordinate: {x: 1, y: 0} as Coordinate,
        //         legalParts: [0, 1],
        //         pointOfCompass: PointOfCompass.NORTH
        //     } as TileDTO,        {
        //         id: TileID.TILE_3,
        //         coordinate: {x: 8, y: 12} as Coordinate,
        //         legalParts: [0, 1],
        //         pointOfCompass: PointOfCompass.NORTH
        //     } as TileDTO
        // ] as TileType[];

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
                        <div className={"tile-wrapper" + (clickable ? " clickable" : "")} onClick={()=>{clickable && move({x, y} as Coordinate)}} style={{height: size, width: size}}>
                            {tile && <Tile legalParts={getLegalPartsForTile({x, y} as Coordinate)} {...tile}/>}
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

        function move(coordinate: Coordinate) {
            let carcassonneMoveDTO = {
                coordinate,
                pointOfCompass
            } as CarcassonneMoveDTO;
            API.move(JSON.stringify(carcassonneMoveDTO));
        }

        function isPositionClickable(coordinate: Coordinate){
            const { playableTilePositions } = carcassonneGameDTO;
            return playableTilePositions && playableTilePositions.some((playableTilePosition) => {
                return playableTilePosition.coordinate.x === coordinate.x
                    && playableTilePosition.coordinate.y === coordinate.y
                    && playableTilePosition.pointOfCompass === pointOfCompass
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

    function NextTile () {


        const [rotating, setRotating] = useState<boolean>(false);

        if (!carcassonneGameDTO.tile || carcassonneGameDTO.nextMoveType !== MoveType.TILE){
            return null;
        }

        return (
            <div className={"next-tile"}>
                <div className={"tile-wrapper " + pointOfCompass.toLowerCase()} style={{height: size, width: size}}>
                    <Tile {...carcassonneGameDTO.tile!}/>
                </div>

                <div className={"rotate-icon" + (rotating ? " rotating" : "")} onClick={()=>rotateLeft()} >
                    <RotateSVG/>
                </div>
            </div>
        );

        function rotateLeft() {
            if (rotating){
                return;
            }
            setRotating(true);
            setTimeout(()=>{
                setRotating(false);
            }, 350);

            switch (pointOfCompass) {
                case PointOfCompass.NORTH:
                    setPointOfCompass(PointOfCompass.WEST);
                    return;
                case PointOfCompass.EAST:
                    setPointOfCompass(PointOfCompass.NORTH);
                    return;
                case PointOfCompass.SOUTH:
                    setPointOfCompass(PointOfCompass.EAST);
                    return;
                case PointOfCompass.WEST:
                    setPointOfCompass(PointOfCompass.SOUTH);
                    return;
            }
        }
    }

    function Game () {
        return(
            <div className={"game"}>
                <Board/>
                <NextTile/>
            </div>
        );
    }

    function End () {
        return null;
    }

    return(
        <div className={"carcassonne"} onMouseMove={(event)=>{onMouseMove(event)}}>
            <Content />
        </div>
    );

}


