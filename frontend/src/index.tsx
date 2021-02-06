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

const profile = Profile.container;

export const IP = profile === Profile.container ? '84.3.87.47' : 'localhost';
export const PORT = Profile.container ? '' : ':8080';
export const ws = Profile.container ? 'wss' : 'ws';

export function log(message?: any, ...optionalParams: any[]) {
    console.log(message, ...optionalParams);
}

ReactDOM.render(
    <BrowserRouter>
        <Route component={App}/>
        {/*<Route component={Dev}/>*/}
    </BrowserRouter>
    , document.getElementById('root')
);

serviceWorker.unregister();
