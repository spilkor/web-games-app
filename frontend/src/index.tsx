import * as React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {BrowserRouter} from "react-router-dom";
import * as serviceWorker from './serviceWorker';
import { Route} from "react-router-dom";

ReactDOM.render(
    <BrowserRouter>
        <Route component={App}/>
    </BrowserRouter>
    ,
    document.getElementById('root'));

serviceWorker.unregister();
