import React, {ReactNode, useEffect} from 'react';

import '../css/header.css';

type ModalProps = {
    children: ReactNode,
    isOpen: boolean,
    closeOnBackGroundClick?: boolean,
    close?: ()=> any,
    onOpen?: ()=> {}
}

export function Modal ({children, isOpen, closeOnBackGroundClick, onOpen, close}: ModalProps) {

    useEffect(() => {
        onOpen && onOpen();
    }, []);

    if (!isOpen){
        return null;
    }

    return (
        <div className={"modal"} >
            <div className={"modal-background"} onClick={()=> {closeOnBackGroundClick && close && close()}}>
            </div>
            <div className={"modal-content"}>
                {children}
            </div>
        </div>
    );
}

















