import React, { useState} from 'react';


import {TileID, TileProps, TileType} from "./carcassonneTypes";
import { PointOfCompass, Position} from "../util/types";
import Tile, {size} from "./Tile";
import './carcassonne.scss';

export function Carcassonne () {

    const [offset_X, setOffset_X] = useState<number>(0);
    const [offset_Y, setOffset_Y] = useState<number>(0);

    const tiles = [
        {
            id: TileID.TILE_0,
            position: {x: 0, y: 0} as Position,
            legalParts: [0, 1],
            pointOfCompass: PointOfCompass.NORTH
        } as TileProps,
        {
            id: TileID.TILE_1,
            position: {x: 0, y: 1} as Position,
            legalParts: [0, 1],
            pointOfCompass: PointOfCompass.NORTH
        } as TileProps,
        {
            id: TileID.TILE_2,
            position: {x: 1, y: 0} as Position,
            legalParts: [0, 1],
            pointOfCompass: PointOfCompass.NORTH
        } as TileProps,        {
            id: TileID.TILE_3,
            position: {x: 8, y: 12} as Position,
            legalParts: [0, 1],
            pointOfCompass: PointOfCompass.NORTH
        } as TileProps
    ] as TileType[];

    const max_x = tiles.reduce(((currentValue, tile)=> {return Math.max(tile.position.x, currentValue)}), 0);
    const min_x = tiles.reduce(((currentValue, tile)=> {return Math.min(tile.position.x, currentValue)}), 0);
    const max_y = tiles.reduce(((currentValue, tile)=> {return Math.max(tile.position.y, currentValue)}), 0);
    const min_y = tiles.reduce(((currentValue, tile)=> {return Math.min(tile.position.y, currentValue)}), 0);

    function getTile(x: number, y: number): TileType | undefined{
        return tiles.find(tile => tile.position.x === x && tile.position.y === y);
    }

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

    const onDragStart = (event: React.DragEvent) => {
        console.log("onDragStart");
        const target = event.target as HTMLDivElement;
        event.dataTransfer!.setData("dragStart_X", event.pageX.toString());
        event.dataTransfer!.setData("dragStart_Y", event.pageY.toString());
        setTimeout(()=>{
            target.style.display = "none";
        },1);
    };

    const onDragEnd = (event: React.DragEvent) => {
        console.log("onDragEnd");
        event.preventDefault();
        const target = event.target as HTMLDivElement;
        target.style.display = "flex";
    };

    const preventDefault = (event: React.DragEvent) => {
        event.preventDefault();
    };

    const onDrop = (event: React.DragEvent) => {
        console.log("onDrop");
        event.preventDefault();
        console.log(offset_X, event.pageX, Number(event.dataTransfer!.getData("dragStart_X")));
        setOffset_X(offset_X + event.pageX - Number(event.dataTransfer!.getData("dragStart_X")));
        setOffset_Y(offset_Y + event.pageY - Number(event.dataTransfer!.getData("dragStart_Y")));
    };

    return(
        <div className={"carcassonne"}
             onDragOver={(event) => preventDefault(event)}
             onDrop={(event)=>{onDrop(event)}}>

            <div className={"board"} draggable
                 onDragStart={(event)=> onDragStart(event)}
                 onDragEnd={(event) => onDragEnd(event)}
                 onDragOver={(event) => preventDefault(event)}
                 style={{translate: offset_X + "px " + offset_Y + "px"}}>

                <div className={"table"}>
                    <table>
                        <tbody>
                        {rows}
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    );

}

export const pointOfCompassMap : {[x: string]:number}= {
    "NORTH": 0,
    "EAST": 90,
    "SOUTH": 180,
    "WEST": 270
};

