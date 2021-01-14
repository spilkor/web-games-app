import React, {useContext, useState} from 'react';


import {CarcassonneGameDTO, TileID, TileDTO} from "./carcassonneTypes";
import {GameState, PointOfCompass, Coordinate} from "../util/types";
import Tile, {size} from "./Tile";
import './carcassonne.scss';
import {AppContext} from "../App";
import {AmobaGameDTO} from "../amoba/amobaTypes";
import {StartGameButton} from "../Main Components/Game";

export function Carcassonne () {

    const { gameData, user } = useContext(AppContext);

    const carcassonneGameDTO = JSON.parse(gameData!.gameJSON) as CarcassonneGameDTO;

    // console.log("carcassonneGameDTO: ", carcassonneGameDTO);

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
                <div>Carcassonee</div>
                {gameData!.owner.id === user!.id &&
                <StartGameButton text={"START"} enabled={gameData!.startable === true}/>
                }
            </div>
        );
    }

    function Board () {

        const tiles = carcassonneGameDTO.tiles;

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
                columns.push(
                    <td key={x}>
                        <div className={"tile-wrapper"} style={{height: size, width: size}}>
                            {tile && <Tile {...tile}/>}
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

    function Game () {
        return(
            <>
                <Board/>
            </>
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


