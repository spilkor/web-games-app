import React, {useState} from 'react';

import './dev.scss';

export function Dev () {

    const [offsetX, _offsetX] = useState<number>(0);
    const offsetXRef = React.useRef(offsetX);
    const setOffsetX = (offsetX: number) => {
        offsetXRef.current = offsetX;
        _offsetX(offsetX);
    };

    const [offsetY, _offsetY] = useState<number>(0);
    const offsetYRef = React.useRef(offsetY);
    const setOffsetY = (offsetY: number) => {
        offsetYRef.current = offsetY;
        _offsetY(offsetY);
    };

    function startDrag() {
        document.addEventListener("mousemove", mouseMoveListener);
        document.addEventListener("mouseup", ()=>{
            document.removeEventListener("mousemove", mouseMoveListener);
        });
    }

    const mouseMoveListener = (event: MouseEvent)=>{
        console.log("move");
        setOffsetX(offsetXRef.current + event.movementX);
        setOffsetY(offsetYRef.current + event.movementY);
    };

    return(
        <div className="dev">
            <div className={"draggable-container-div"}>
                <svg className={"draggable-container-svg"}>
                    <rect className={"draggable-rect"} height={100} width={100} x={offsetXRef.current} y={offsetYRef.current} onMouseDown={(e) => startDrag()}/>
                </svg>
            </div>
        </div>
    );
}