import * as React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {BrowserRouter} from "react-router-dom";
import * as serviceWorker from './serviceWorker';
import { Route} from "react-router-dom";

enum Profile  {
    container,
    developer
}

const profile = Profile.developer as Profile;

const IP = profile === Profile.container ? 'spilkor.com' : 'localhost';
const PORT = profile === Profile.container ? '' : ':8080';
const WS = profile === Profile.container ? 'wss' : 'ws';
export const WEBSOCKET_URL = WS + '://' + IP + PORT + '/websocket';


export function log(message?: any, ...optionalParams: any[]) {
    profile === Profile.developer && console.log(message, ...optionalParams);
}

ReactDOM.render(
    <BrowserRouter>
        <Route component={App}/>
    </BrowserRouter>
    , document.getElementById('root')
);

serviceWorker.unregister();
