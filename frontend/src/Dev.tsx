import React from 'react';

import './css/app.css';
import './css/header.css';
import {Header} from "./Main Components/Header";
import { ContentMode} from "./App";

import {Carcassonne} from "./carcassonne/Carcassonne";

export function Dev () {
    return(
        <div className="app">
            <Header />
            <div className={"content " + ContentMode.GAME.toLowerCase() + "-content"}>

                <Carcassonne/>

            </div>
        </div>
    );
}