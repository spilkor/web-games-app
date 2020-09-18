import * as React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {BrowserRouter} from "react-router-dom";
import * as serviceWorker from './serviceWorker';
import { Route} from "react-router-dom";


export const IP = 'localhost'; export const PORT = ':8080';
// export const IP = '84.3.87.47'; export const PORT = '';


export function log(content : string) {
    console.log(content);
}

ReactDOM.render(
    <BrowserRouter>
        <Route component={App}/>
    </BrowserRouter>
    , document.getElementById('root')
);

serviceWorker.unregister();
