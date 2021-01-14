import React from 'react';

import {Coordinate, PointOfCompass} from "../util/types";
import {
    MeepleProps,
    MonasteryProps,
    pointOfCompassMap,
    ShieldProps,
    TileID,
    TileDTO,
    MeepleType, Color
} from "./carcassonneTypes";

export const size = 100;

export default function Tile (tileProps: TileDTO) {

    switch (tileProps.id) {
        case TileID.TILE_0: return( <Tile_0 {...tileProps} />);
        case TileID.TILE_1: return( <Tile_1 {...tileProps}/>);
        case TileID.TILE_2: return( <Tile_2 {...tileProps}/>);
        case TileID.TILE_3: return( <Tile_3 {...tileProps}/>);
        case TileID.TILE_4: return( <Tile_4 {...tileProps}/>);
        case TileID.TILE_5: return( <Tile_5 {...tileProps}/>);
        case TileID.TILE_6: return( <Tile_6 {...tileProps}/>);
        case TileID.TILE_7: return( <Tile_7 {...tileProps}/>);
        case TileID.TILE_8: return( <Tile_8 {...tileProps}/>);
        case TileID.TILE_9: return( <Tile_9 {...tileProps}/>);
        case TileID.TILE_10: return(<Tile_10 {...tileProps}/>);
        case TileID.TILE_11: return(<Tile_11 {...tileProps}/>);
        case TileID.TILE_12: return(<Tile_12 {...tileProps}/>);
        case TileID.TILE_13: return(<Tile_13 {...tileProps}/>);
        case TileID.TILE_14: return(<Tile_14 {...tileProps}/>);
        case TileID.TILE_15: return(<Tile_15 {...tileProps}/>);
        case TileID.TILE_16: return(<Tile_16 {...tileProps}/>);
        case TileID.TILE_17: return(<Tile_17 {...tileProps}/>);
        case TileID.TILE_18: return(<Tile_18 {...tileProps}/>);
        case TileID.TILE_19: return(<Tile_19 {...tileProps}/>);
        case TileID.TILE_20: return(<Tile_20 {...tileProps}/>);
        case TileID.TILE_21: return(<Tile_21 {...tileProps}/>);
        case TileID.TILE_22: return(<Tile_22 {...tileProps}/>);
        case TileID.TILE_23: return(<Tile_23 {...tileProps}/>);

        default: return tileProps.id;
    }

}

function Shield({position}: ShieldProps){
    return(
        <polygon className={"shield"} transform={"translate(" + (size*position.x) + " " + (size*position.y) + ")"} points={"-" + (size/10) + "-" + (size/10) + " " + (size/10) + " -" + (size/10) + " " + 0 + " " + (size/10)}/>
    );
}

function House(){
    return(
        <g className={"house"} >
            <rect x={size/2 - size/8} y={size/2 - size/8} width={size/4} height={size/4}/>
        </g>
    );
}

function Monastery({partIndex, legalParts}: MonasteryProps){
    const width = size*2/5;
    return(
        <g className={"monastery" + clickable(legalParts, partIndex)} >
            <rect x={size/2 - width/2} y={size/2 - width/2} width={width} height={width}/>
            {/*<polygon points={(size/2 - size/8 - 5) + " " + (size/2 - size/8 + 1) + " " + (size/2 + size/8 + 5) + " " + (size/2 - size/8 + 1) + " " + (size/2) + " " + (size/2-size/4)}/>*/}
        </g>
    );
}

function Meeple({position, color, pointOfCompass}: MeepleProps){
    return(
        <svg height={size} width={size} className={"meeple " + color.toLowerCase()} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
            <path transform={"translate(" + position.x*size + " " + position.y*size + ") scale(" + size/100 + ") rotate(" + -pointOfCompassMap[pointOfCompass] + ")"} d="M 0.000 -15.704 c -2.109 0.000 -3.626 1.116 -4.503 2.518 -0.784 1.254 -1.110 2.708 -1.173 3.981 -2.391 1.182 -5.040 2.375 -7.173 3.593 -1.119 0.639 -2.094 1.282 -2.828 1.973 C -16.411 -2.947 -16.953 -2.169 -16.953 -1.251 c 0.000 0.391 0.191 0.709 0.405 0.943 0.215 0.234 0.473 0.415 0.767 0.584 0.587 0.339 1.327 0.621 2.142 0.865 1.210 0.363 2.564 0.633 3.742 0.748 -1.172 2.019 -2.731 3.873 -4.097 5.650 C -15.574 9.593 -16.953 11.562 -16.953 13.749 c 0.000 0.313 -0.003 0.556 0.024 0.802 0.028 0.245 0.098 0.551 0.345 0.793 0.246 0.243 0.548 0.308 0.794 0.334 0.246 0.027 0.493 0.024 0.811 0.024 h 8.714 c 0.633 0.000 1.096 0.038 1.572 -0.268 0.477 -0.306 0.650 -0.725 0.990 -1.350 l 0.007 -0.013 0.006 -0.013 s 0.760 -1.545 1.666 -3.077 c 0.453 -0.766 0.945 -1.529 1.373 -2.070 0.214 -0.270 0.414 -0.485 0.559 -0.606 0.044 -0.037 0.064 -0.044 0.093 -0.059 0.028 0.016 0.048 0.023 0.093 0.059 0.144 0.121 0.345 0.335 0.559 0.606 0.428 0.541 0.920 1.304 1.373 2.070 0.906 1.533 1.666 3.077 1.666 3.077 l 0.006 0.013 0.007 0.013 c 0.340 0.625 0.512 1.041 0.985 1.348 0.473 0.307 0.935 0.270 1.559 0.270 H 15.000 c 0.313 0.000 0.556 0.003 0.800 -0.024 0.244 -0.027 0.547 -0.095 0.791 -0.338 0.244 -0.244 0.311 -0.547 0.338 -0.791 0.027 -0.244 0.024 -0.488 0.024 -0.800 0.000 -2.188 -1.379 -4.157 -2.958 -6.210 -1.367 -1.777 -2.926 -3.631 -4.097 -5.650 1.178 -0.115 2.533 -0.385 3.742 -0.748 0.814 -0.244 1.554 -0.527 2.142 -0.865 0.294 -0.169 0.551 -0.351 0.766 -0.584 0.215 -0.234 0.406 -0.552 0.406 -0.943 0.000 -0.918 -0.542 -1.696 -1.276 -2.387 -0.734 -0.691 -1.710 -1.334 -2.828 -1.973 -2.133 -1.219 -4.782 -2.411 -7.173 -3.593 -0.064 -1.273 -0.390 -2.728 -1.173 -3.981 C 3.626 -14.588 2.109 -15.704 0.000 -15.704 z"/>
        </svg>
    );
}

function clickable(legalParts: number[] | null, n: number) {
    return legalParts && legalParts.some((legalPart) => legalPart === n) ? " clickable" : "";
}

function Tile_0 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[2] = {x: 1/5, y: 3/10} as Coordinate;
    meeplePositionMap[3] = {x: 1/2, y: 3/4} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size/2}/>
                <rect className={"field" + clickable(legalParts,3)} x={0} y={size/2} width={size} height={size/2}/>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={0} y1={size/2} x2={size} y2={size/2} />
                    <line className={"inside"} x1={0} y1={size/2} x2={size} y2={size/2} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_1 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/3, y: 1/3} as Coordinate;
    meeplePositionMap[1] = {x: 3/5, y: 3/5} as Coordinate;
    meeplePositionMap[2] = {x: 1/5, y: 1/6} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <path className={"field" + clickable(legalParts,1)} d={"M 0 0 L " + (size/2) + " 0 A " + (size/2) + " " + (size/2) + " 0 0 1 0 " + (size/2) + " L 0 " + size + " L " + size + " " + size + " L " + size + " 0"}/>
                <path className={"field" + clickable(legalParts,2)} d={"M 0 0 L " + (size/2) + " 0 A " + (size/2) + " " + (size/2) + " 0 0 1 0 " + (size/2)}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <path className={"border"} fill="none" d={"M " + (size/2) + ",0 A " + (size/2) + " " + (size/2) + " 0 0 1 0 " + (size/2)} />
                    <path className={"inside"} fill="none" d={"M " + (size/2) + ",0 A " + (size/2) + " " + (size/2) + " 0 0 1 0 " + (size/2)} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_2 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 1/5} as Coordinate;
    meeplePositionMap[2] = {x: 1/2, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size/2}/>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={size/2} width={size} height={size/2}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={0} y1={size/2} x2={size} y2={size/2} />
                    <line className={"inside"} x1={0} y1={size/2} x2={size} y2={size/2} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_3 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/5} as Coordinate;
    meeplePositionMap[1] = {x: 4/5, y: 1/2} as Coordinate;
    meeplePositionMap[2] = {x: 1/2, y: 4/5} as Coordinate;
    meeplePositionMap[3] = {x: 1/5, y: 1/2} as Coordinate;
    meeplePositionMap[4] = {x: 4/5, y: 1/5} as Coordinate;
    meeplePositionMap[5] = {x: 4/5, y: 4/5} as Coordinate;
    meeplePositionMap[6] = {x: 1/5, y: 4/5} as Coordinate;
    meeplePositionMap[7] = {x: 1/5, y: 1/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,4)} x={size/2} y={0} width={size/2} height={size/2}/>
                <rect className={"field" + clickable(legalParts,5)} x={size/2} y={size/2} width={size/2} height={size/2}/>
                <rect className={"field" + clickable(legalParts,6)} x={0} y={size/2} width={size/2} height={size/2}/>
                <rect className={"field" + clickable(legalParts,7)} x={0} y={0} width={size/2} height={size/2}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={size/2} y1={0} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size/2} y1={0} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,1)}>
                    <line className={"border"} x1={size} y1={size/2} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size} y1={size/2} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,2)}>
                    <line className={"border"} x1={size/2} y1={size} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size/2} y1={size} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,3)}>
                    <line className={"border"} x1={0} y1={size/2} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={0} y1={size/2} x2={size/2} y2={size/2} />
                </g>
                <House/>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_4 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 4/5, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 4/5} as Coordinate;
    meeplePositionMap[2] = {x: 1/5, y: 1/2} as Coordinate;
    meeplePositionMap[3] = {x: 1/2, y: 1/5} as Coordinate;
    meeplePositionMap[4] = {x: 4/5, y: 4/5} as Coordinate;
    meeplePositionMap[5] = {x: 1/5, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,3)} x={0} y={0} width={size} height={size/2}/>
                <rect className={"field" + clickable(legalParts,4)} x={size/2} y={size/2} width={size/2} height={size/2}/>
                <rect className={"field" + clickable(legalParts,5)} x={0} y={size/2} width={size/2} height={size/2}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={size} y1={size/2} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size} y1={size/2} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,1)}>
                    <line className={"border"} x1={size/2} y1={size} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size/2} y1={size} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,2)}>
                    <line className={"border"} x1={0} y1={size/2} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={0} y1={size/2} x2={size/2} y2={size/2} />
                </g>
                <House/>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_5 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/6, y: 1/6} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,0)} x={0} y={0} width={size} height={size}/>
                <Monastery legalParts={legalParts} partIndex={1}/>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_6 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 4/5} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[2] = {x: 1/6, y: 1/6} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={size/2} y1={size} x2={size/2} y2={size/2}/>
                    <line className={"inside"} x1={size/2} y1={size} x2={size/2} y2={size/2}/>
                </g>
                <Monastery legalParts={legalParts} partIndex={2}/>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_7 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0 L " + (size*2) + " " + (size*2) + " L -" + size + " " + (size*2) + " z"} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_8 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0 L " + (size*2) + " " + (size*2) + " L -" + size + " " + (size*2) + " z"} />
                    <Shield position={{x: 1/6, y: 5/6} as Coordinate}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_9 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <g className={"city" + clickable(legalParts,0)}>
                    <rect x={0} y={0} width={size} height={size}/>
                    <Shield position={{x: 1/6, y: 5/6} as Coordinate}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_10 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/4, y: 1/4} as Coordinate;
    meeplePositionMap[1] = {x: 3/4, y: 3/4} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <polygon points={"-5 -5 " + (size + 5) + " -5 -5 " + (size + 5)}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_11 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/4, y: 1/4} as Coordinate;
    meeplePositionMap[1] = {x: 3/4, y: 3/4} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <polygon points={"-5 -5 " + (size + 5) + " -5 -5 " + (size + 5)}/>
                    <Shield position={{x: 1/6, y: 7/12} as Coordinate}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_12 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 2/3, y: 2/3} as Coordinate;
    meeplePositionMap[1] = {x: 1/4, y: 1/4} as Coordinate;
    meeplePositionMap[2] = {x: 5/6, y: 1/3} as Coordinate;
    meeplePositionMap[3] = {x: 5/6, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size}/>
                <path className={"field" + clickable(legalParts,3)} d={"M " + size + " " + size/2 + " A " + size/2 + " " + size/2 + " 0 0 0 " + size/2 + " " + size + " L " + size + " " + size}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <path className={"border"} fill="none" d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size}/>
                    <path className={"inside"} fill="none" d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size}/>
                </g>
                <g className={"city" + clickable(legalParts,1)}>
                    <polygon points={"-5 -5 " + (size+5) + " -5 -5 " + (size+5)}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_13 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 2/3, y: 2/3} as Coordinate;
    meeplePositionMap[1] = {x: 1/4, y: 1/4} as Coordinate;
    meeplePositionMap[2] = {x: 5/6, y: 1/3} as Coordinate;
    meeplePositionMap[3] = {x: 5/6, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size}/>
                <path className={"field" + clickable(legalParts,3)} d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size + " L " + size + " " + size}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <path className={"border"} fill="none" d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size}/>
                    <path className={"inside"} fill="none" d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size}/>
                </g>
                <g className={"city" + clickable(legalParts, 1)}>
                    <polygon points={"-5 -5 " + (size+5) + " -5 -5 " + (size+5)}/>
                    <Shield position={{x: 1/6, y: 7/12} as Coordinate}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_14 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[1] = {x: 0, y: 1/2} as Coordinate;
    meeplePositionMap[2] = {x: 1/2, y: 1/2} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + ", " + (size*30/100) + " " + (size*70/100) + " 0 " + size} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_15 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 1/2} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_16 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 4/5, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 4/5} as Coordinate;
    meeplePositionMap[2] = {x: 1/5, y: 1/2} as Coordinate;
    meeplePositionMap[3] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[4] = {x: 1/5, y: 3/10} as Coordinate;
    meeplePositionMap[5] = {x: 4/5, y: 4/5} as Coordinate;
    meeplePositionMap[6] = {x: 1/5, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,4)} x={0} y={0} width={size} height={size/2}/>
                <rect className={"field" + clickable(legalParts,5)} x={size/2} y={size/2} width={size/2} height={size/2}/>
                <rect className={"field" + clickable(legalParts,6)} x={0} y={size/2} width={size/2} height={size/2}/>
                <g className={"city" + clickable(legalParts,3)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={size} y1={size/2} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size} y1={size/2} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,1)}>
                    <line className={"border"} x1={size/2} y1={size} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size/2} y1={size} x2={size/2} y2={size/2} />
                </g>
                <g className={"road" + clickable(legalParts,2)}>
                    <line className={"border"} x1={0} y1={size/2} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={0} y1={size/2} x2={size/2} y2={size/2} />
                </g>
                <House/>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_17 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 2/3, y: 2/3} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[2] = {x: 2/7, y: 1/2} as Coordinate;
    meeplePositionMap[3] = {x: 5/6, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size}/>
                <path className={"field" + clickable(legalParts,3)} d={"M " + size + " " + size/2 + " A " + size/2 + " " + size/2 + " 0 0 0 " + size/2 + " " + size + " L " + size + " " + size}/>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
                <g className={"road" + clickable(legalParts,0)}>
                    <path className={"border"} fill="none" d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size}/>
                    <path className={"inside"} fill="none" d={"M " + size + " " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 0 " + (size/2) + " " + size}/>
                </g>

            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_18 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/3, y: 2/3} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[2] = {x: 5/7, y: 1/2} as Coordinate;
    meeplePositionMap[3] = {x: 1/6, y: 4/5} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size}/>
                <path className={"field" + clickable(legalParts,3)} d={"M 0 " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 1 " + (size/2) + " " + size + " L 0 " + size}/>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
                <g className={"road" + clickable(legalParts,0)}>
                    <path className={"border"} fill="none" d={"M 0 " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 1 " + (size/2) + " " + size}/>
                    <path className={"inside"} fill="none" d={"M 0 " + (size/2) + " A " + (size/2) + " " + (size/2) + " 0 0 1 " + (size/2) + " " + size}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_19 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[1] = {x: 0, y: 1/2} as Coordinate;
    meeplePositionMap[2] = {x: 1/2, y: 1/2} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={0} width={size} height={size}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0"} />
                </g>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " 0 " + size} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_20 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[2] = {x: 4/5, y: 0} as Coordinate;
    meeplePositionMap[3] = {x: 1/5, y: 0} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={size/2} y={0} width={size} height={size}/>
                <rect className={"field" + clickable(legalParts,3)} x={0} y={0} width={size/2} height={size}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={size/2} y1={0} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size/2} y1={0} x2={size/2} y2={size/2} />
                </g>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0 L " + (size*2) + " " + (size*2) + " L -" + size + " " + (size*2) + " z"} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_21 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[2] = {x: 4/5, y: 0} as Coordinate;
    meeplePositionMap[3] = {x: 1/5, y: 0} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,2)} x={size/2} y={0} width={size} height={size}/>
                <rect className={"field" + clickable(legalParts,3)} x={0} y={0} width={size/2} height={size}/>
                <g className={"road" + clickable(legalParts,0)}>
                    <line className={"border"} x1={size/2} y1={0} x2={size/2} y2={size/2} />
                    <line className={"inside"} x1={size/2} y1={0} x2={size/2} y2={size/2} />
                </g>
                <g className={"city" + clickable(legalParts,1)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0 L " + (size*2) + " " + (size*2) + " L -" + size + " " + (size*2) + " z"} />
                    <Shield position={{x: 1/6, y: 5/6} as Coordinate}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_22 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[2] = {x: 1/2, y: 1} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size/2}/>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={size/2} width={size} height={size/2}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0 L " + size*2 + " " + size + " L " + size + " " + size + " C " + (size*70/100) + " " + (size*70/100) + " " + (size*30/100) + " " + (size*70/100) + " 0 " + size} />
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}

function Tile_23 ({pointOfCompass, meeple, legalParts } : TileDTO) {

    const meeplePositionMap = [] as Coordinate[];
    meeplePositionMap[0] = {x: 1/2, y: 1/2} as Coordinate;
    meeplePositionMap[1] = {x: 1/2, y: 0} as Coordinate;
    meeplePositionMap[2] = {x: 1/2, y: 1} as Coordinate;

    return(
        <>
            <svg className={"tile"} height={size} width={size} transform={"rotate(" + pointOfCompassMap[pointOfCompass] + ")"}>
                <rect className={"field" + clickable(legalParts,1)} x={0} y={0} width={size} height={size/2}/>
                <rect className={"field" + clickable(legalParts,2)} x={0} y={size/2} width={size} height={size/2}/>
                <g className={"city" + clickable(legalParts,0)}>
                    <path d={"M 0 0 C " + (size*30/100) + " " + (size*30/100) + " " + (size*70/100) + " " + (size*30/100) + " " + size + " 0 L " + size*2 + " " + size + " L " + size + " " + size + " C " + (size*70/100) + " " + (size*70/100) + " " + (size*30/100) + " " + (size*70/100) + " 0 " + size} />
                    <Shield position={{x: 1/6, y: 4/6} as Coordinate}/>
                </g>
            </svg>
            {meeple && <Meeple pointOfCompass={pointOfCompass} color={meeple.color} position={meeplePositionMap[meeple.position]}/>}
        </>
    );
}
