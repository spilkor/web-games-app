import React, {useContext, useState} from 'react';


import {GameState} from "../../util/types";
import './snapszer.scss';
import {StartGameButton} from "../Game";
import {AppContext} from "../../App";
import {
    Act,
    Card,
    Color,
    Figure,
    GameStatus,
    Player,
    SnapszerGameDTO,
    SnapszerGameSettingsDTO,
    SnapszerMoveDTO,
    TurnValue
} from "./snapszerTypes";

import makkasz from "./png/makkasz.png";
import makktiz from "./png/makktiz.png";
import makkkiraly from "./png/makkkiraly.png";
import makkfelso from "./png/makkfelso.png";
import makkalso from "./png/makkalso.png";
import makkkilenc from "./png/makkkilenc.png";

import tokasz from "./png/tokasz.png";
import toktiz from "./png/toktiz.png";
import tokkiraly from "./png/tokkiraly.png";
import tokfelso from "./png/tokfelso.png";
import tokalso from "./png/tokalso.png";
import tokkilenc from "./png/tokkilenc.png";

import korasz from "./png/korasz.png";
import kortiz from "./png/kortiz.png";
import korkiraly from "./png/korkiraly.png";
import korfelso from "./png/korfelso.png";
import koralso from "./png/koralso.png";
import korkilenc from "./png/korkilenc.png";

import zoldasz from "./png/zoldasz.png";
import zoldtiz from "./png/zoldtiz.png";
import zoldkiraly from "./png/zoldkiraly.png";
import zoldfelso from "./png/zoldfelso.png";
import zoldalso from "./png/zoldalso.png";
import zoldkilenc from "./png/zoldkilenc.png";

import cardback from "./png/cardback.png";

import hand_0 from "./png/hand_0.png";
import hand_1 from "./png/hand_1.png";
import hand_2 from "./png/hand_2.png";
import hand_3 from "./png/hand_3.png";
import hand_4 from "./png/hand_4.png";
import hand_5 from "./png/hand_5.png";
import hand_6 from "./png/hand_6.png";

import csap from "./png/csap.png";

import joker from "./png/joker.png";
import {Modal} from "../../modal/Modal";
import API from "../../util/API";

import {ReactComponent as HandSVG} from './svg/hand.svg';

export function Snapszer () {

    const { gameData, user, gameSettings, setGameSettings  } = useContext(AppContext);

    const snapszerGameDTO = JSON.parse(gameData!.gameJSON) as SnapszerGameDTO;

    const {players, caller, nextPlayer, gameStatus, csapCards, csapIndex, snapszer, turnValue, round, calledCard} = snapszerGameDTO;

    const myTurn = nextPlayer && nextPlayer.user.id == user!.id;

    const myPlayer = players.find(player => player.user.id === user!.id) as Player;
    const myIndex = players.indexOf(myPlayer);

    const leftPlayer = players[getIndex(myIndex+1)];
    const oppositePlayer = players[getIndex(myIndex+2)];
    const rightPlayer = players[getIndex(myIndex+3)];

    const [jokerOpen, setJokerOpen] = useState<boolean>(false);

    const [showPlayerWonCards, setShowPlayerWonCards] = useState<Player>();


    const tempSetting = gameSettings as SnapszerGameSettingsDTO;
    const snapszerGameSettingsDTO = {
        lastRound: tempSetting && tempSetting.lastRound ? tempSetting.lastRound : null,
    } as SnapszerGameSettingsDTO;
    const {
        lastRound
    } = snapszerGameSettingsDTO;

    const lastRoundChanged = snapszerGameDTO.lastRound && snapszerGameDTO.lastRound.find((c: Card) => {return lastRound != null && !lastRound.find((c2: Card)=>{return c.figure === c2.figure && c.color === c2.color})});

    lastRoundChanged && setGameSettings!(snapszerGameSettingsDTO);

    function getIndex(index: number) {
        if (index < 0){
            return players.length + index;
        } else if(index > players.length -1){
            return index - players.length;
        } else {
            return index;
        }
    }

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
                <div className={"caption"}>Snapszer lobby</div>
                <div className={"row"}>
                    <div className={"key"}>
                        Owner:
                    </div>
                    <div className={"value"}>
                        <span>{gameData!.owner.name}</span>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"key"}>
                        Players:
                    </div>
                    <div className={"value"}>
                        {gameData!.players.map((user, key)=>
                            <div key={key}><span>{user.name}</span></div>
                        )}
                    </div>
                </div>
                {gameData!.owner.id === user!.id &&
                <StartGameButton text={"START"} enabled={gameData!.startable === true}/>
                }
            </div>
        );

    }

    function Board () {
        return(
            <div className={"board"} >
                <LeftHand/>
                <OppositeHand/>
                <RightHand/>
                {round && <Round/>}
                {lastRound && <LastRound/>}
                {(gameStatus === GameStatus.CALL_CARD || gameStatus === GameStatus.CALL_FIGURE) &&
                    <Csap/>
                }
                {myTurn && (gameStatus === GameStatus.CALL_CARD || gameStatus === GameStatus.CALL_FIGURE) &&
                    <Joker/>
                }
                {myTurn && (gameStatus === GameStatus.FIRST_ACT || gameStatus === GameStatus.ACT) &&
                <ActComp/>
                }
                <MyHand/>
                <WoundRounds/>
                <CalledCard/>
                {myTurn && gameStatus === GameStatus.PLAY_CARD && myPlayer.wonRounds && myPlayer.wonRounds.length !== 0 && <Count/>}
            </div>
        );
    }

    function LeftHand () {
        return(
            <div className={"left-hand"} >
                <Hand handSize={leftPlayer.cards.length}/>
                <PlayerName userName={leftPlayer.user.name}/>
            </div>
        );
    }

    type HandProps = {
        handSize: number
    }

    function OppositeHand () {
        return(
            <div className={"opposite-hand"} >
                <Hand handSize={oppositePlayer.cards.length}/>
                <PlayerName userName={oppositePlayer.user.name}/>
            </div>
        );
    }

    function RightHand () {
        return(
            <div className={"right-hand"} >
                <Hand handSize={rightPlayer.cards.length}/>
                <PlayerName userName={rightPlayer.user.name}/>
            </div>
        );
    }

    type PlayerNameProps = {
        userName: string
    }

    function PlayerName({userName}: PlayerNameProps) {
        return (
            <div className={"player-name-hand"}>
                {userName}
            </div>
        );
    }


    function Hand({handSize}: HandProps) {
        switch (handSize) {
            case 1:
                return <img alt="hand_1" src={hand_1}/>;
            case 2:
                return <img alt="hand_2" src={hand_2}/>;
            case 3:
                return <img alt="hand_3" src={hand_3}/>;
            case 4:
                return <img alt="hand_4" src={hand_4}/>;
            case 5:
                return <img alt="hand_5" src={hand_5}/>;
            case 6:
                return <img alt="hand_6" src={hand_6}/>;
            default:
                return <img alt="hand_0" src={hand_0}/>;
        }
    }

    function Joker () {
        return(
            <>
                {jokerOpen ||
                <div className={"joker"} >
                    <img alt = "joker" src={joker} onClick={()=> setJokerOpen(true)}/>
                </div>
                }
                {jokerOpen &&
                <Modal isOpen={jokerOpen} closeOnBackGroundClick={true} close={()=> {setJokerOpen(false)}}>
                    {gameStatus === GameStatus.CALL_CARD ? <CallCard/> : <CallFigure/>}
                </Modal>
                }
            </>
        );

        function CallCard () {
            return(
                <div className={"call-card"} >
                    <div className={"row"}>
                        <img alt = "makkasz" src={makkasz} onClick={()=> callCard({color: Color.MAKK, figure: Figure.ASZ} as Card)}/>
                        <img alt = "makktiz" src={makktiz} onClick={()=> callCard({color: Color.MAKK, figure: Figure.TIZ} as Card)}/>
                        <img alt = "makkkiraly" src={makkkiraly} onClick={()=> callCard({color: Color.MAKK, figure: Figure.KIRALY} as Card)}/>
                        <img alt = "makkfelso" src={makkfelso} onClick={()=> callCard({color: Color.MAKK, figure: Figure.FELSO} as Card)}/>
                        <img alt = "makkalso" src={makkalso} onClick={()=> callCard({color: Color.MAKK, figure: Figure.ALSO} as Card)}/>
                        <img alt = "makkkilenc" src={makkkilenc} onClick={()=> callCard({color: Color.MAKK, figure: Figure.KILENC} as Card)}/>
                    </div>
                    <div className={"row"}>
                        <img alt = "tokasz" src={tokasz} onClick={()=> callCard({color: Color.TOK, figure: Figure.ASZ} as Card)}/>
                        <img alt = "toktiz" src={toktiz} onClick={()=> callCard({color: Color.TOK, figure: Figure.TIZ} as Card)}/>
                        <img alt = "tokkiraly" src={tokkiraly} onClick={()=> callCard({color: Color.TOK, figure: Figure.KIRALY} as Card)}/>
                        <img alt = "tokfelso" src={tokfelso} onClick={()=> callCard({color: Color.TOK, figure: Figure.FELSO} as Card)}/>
                        <img alt = "tokalso" src={tokalso} onClick={()=> callCard({color: Color.TOK, figure: Figure.ALSO} as Card)}/>
                        <img alt = "tokkilenc" src={tokkilenc} onClick={()=> callCard({color: Color.TOK, figure: Figure.KILENC} as Card)}/>
                    </div>
                    <div className={"row"}>
                        <img alt = "korasz" src={korasz} onClick={()=> callCard({color: Color.KOR, figure: Figure.ASZ} as Card)}/>
                        <img alt = "kortiz" src={kortiz} onClick={()=> callCard({color: Color.KOR, figure: Figure.TIZ} as Card)}/>
                        <img alt = "korkiraly" src={korkiraly} onClick={()=> callCard({color: Color.KOR, figure: Figure.KIRALY} as Card)}/>
                        <img alt = "korfelso" src={korfelso} onClick={()=> callCard({color: Color.KOR, figure: Figure.FELSO} as Card)}/>
                        <img alt = "koralso" src={koralso} onClick={()=> callCard({color: Color.KOR, figure: Figure.ALSO} as Card)}/>
                        <img alt = "korkilenc" src={korkilenc} onClick={()=> callCard({color: Color.KOR, figure: Figure.KILENC} as Card)}/>
                    </div>
                    <div className={"row"}>
                        <img alt = "zoldasz" src={zoldasz} onClick={()=> callCard({color: Color.ZOLD, figure: Figure.ASZ} as Card)}/>
                        <img alt = "zoldtiz" src={zoldtiz} onClick={()=> callCard({color: Color.ZOLD, figure: Figure.TIZ} as Card)}/>
                        <img alt = "zoldkiraly" src={zoldkiraly} onClick={()=> callCard({color: Color.ZOLD, figure: Figure.KIRALY} as Card)}/>
                        <img alt = "zoldfelso" src={zoldfelso} onClick={()=> callCard({color: Color.ZOLD, figure: Figure.FELSO} as Card)}/>
                        <img alt = "zoldalso" src={zoldalso} onClick={()=> callCard({color: Color.ZOLD, figure: Figure.ALSO} as Card)}/>
                        <img alt = "zoldkilenc" src={zoldkilenc} onClick={()=> callCard({color: Color.ZOLD, figure: Figure.KILENC} as Card)}/>
                    </div>
                </div>
            );
        }

        function CallFigure () {
            const csapColor = csapCards![csapIndex!].color;
            return(
                <div className={"call-card"} >
                    {csapColor == Color.MAKK &&
                    <div className={"row"}>
                        <img alt = "makkasz" src={makkasz} onClick={()=> callFigure(Figure.ASZ)}/>
                        <img alt = "makktiz" src={makktiz} onClick={()=> callFigure(Figure.TIZ)}/>
                        <img alt = "makkkiraly" src={makkkiraly} onClick={()=> callFigure(Figure.KIRALY)}/>
                        <img alt = "makkfelso" src={makkfelso} onClick={()=> callFigure(Figure.FELSO)}/>
                        <img alt = "makkalso" src={makkalso} onClick={()=> callFigure(Figure.ALSO)}/>
                        <img alt = "makkkilenc" src={makkkilenc} onClick={()=> callFigure(Figure.KILENC)}/>
                    </div>
                    }

                    {csapColor == Color.TOK &&
                    <div className={"row"}>
                        <img alt = "tokasz" src={tokasz} onClick={()=> callFigure(Figure.ASZ)}/>
                        <img alt = "toktiz" src={toktiz} onClick={()=> callFigure(Figure.TIZ)}/>
                        <img alt = "tokkiraly" src={tokkiraly} onClick={()=> callFigure(Figure.KIRALY)}/>
                        <img alt = "tokfelso" src={tokfelso} onClick={()=> callFigure(Figure.FELSO)}/>
                        <img alt = "tokalso" src={tokalso} onClick={()=> callFigure(Figure.ALSO)}/>
                        <img alt = "tokkilenc" src={tokkilenc} onClick={()=> callFigure(Figure.KILENC)}/>
                    </div>
                    }

                    {csapColor == Color.KOR &&
                    <div className={"row"}>
                        <img alt = "korasz" src={korasz} onClick={()=> callFigure(Figure.ASZ)}/>
                        <img alt = "kortiz" src={kortiz} onClick={()=> callFigure(Figure.TIZ)}/>
                        <img alt = "korkiraly" src={korkiraly} onClick={()=> callFigure(Figure.KIRALY)}/>
                        <img alt = "korfelso" src={korfelso} onClick={()=> callFigure(Figure.FELSO)}/>
                        <img alt = "koralso" src={koralso} onClick={()=> callFigure(Figure.ALSO)}/>
                        <img alt = "korkilenc" src={korkilenc} onClick={()=> callFigure(Figure.KILENC)}/>
                    </div>
                    }

                    {csapColor == Color.ZOLD &&
                    <div className={"row"}>
                        <img alt = "zoldasz" src={zoldasz} onClick={()=> callFigure(Figure.ASZ)}/>
                        <img alt = "zoldtiz" src={zoldtiz} onClick={()=> callFigure(Figure.TIZ)}/>
                        <img alt = "zoldkiraly" src={zoldkiraly} onClick={()=> callFigure(Figure.KIRALY)}/>
                        <img alt = "zoldfelso" src={zoldfelso} onClick={()=> callFigure(Figure.FELSO)}/>
                        <img alt = "zoldalso" src={zoldalso} onClick={()=> callFigure(Figure.ALSO)}/>
                        <img alt = "zoldkilenc" src={zoldkilenc} onClick={()=> callFigure(Figure.KILENC)}/>
                    </div>
                    }
                </div>
            );
        }
    }

    function ActComp () {
        return(
            <div className={"act"} >
                <div className={"act-option"} onClick={()=> act(Act.CHECK)}>
                    Check
                </div>
                {(myPlayer.withCaller === true && !snapszer) &&
                <div className={"act-option"} onClick={()=> act(Act.SNAPSZER)}>
                    Snapszer
                </div>
                }
                <Contra/>
                {gameStatus === GameStatus.FIRST_ACT &&
                <div className={"act-option"} onClick={()=> act(Act.THROW_IN)}>
                    Throw in
                </div>
                }
            </div>
        );
    }

    function Contra () {
        if (gameStatus == GameStatus.FIRST_ACT){
            return null;
        }
        switch (turnValue + "") {
            case TurnValue.BASIC:
                if (myPlayer.withCaller){
                    return null;
                } else {
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.KONTRA)}
                        </div>
                    );
                }
            case TurnValue.KONTRA:
                if (myPlayer.withCaller){
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.RE_KONTRA)}
                        </div>
                    );
                } else {
                    return null;
                }
            case TurnValue.RE_KONTRA:
                if (myPlayer.withCaller){
                    return null;
                } else {
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.SZUB_KONTRA)}
                        </div>
                    );
                }
            case TurnValue.SZUB_KONTRA:
                if (myPlayer.withCaller){
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.MORD_KONTRA)}
                        </div>
                    );
                } else {
                    return null;
                }
            case TurnValue.MORD_KONTRA:
                if (myPlayer.withCaller){
                    return null;
                } else {
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.HIRSCH_KONTRA)}
                        </div>
                    );
                }
            case TurnValue.HIRSCH_KONTRA:
                if (myPlayer.withCaller){
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.FEDAK_SARI)}
                        </div>
                    );
                } else {
                    return null;
                }
            case TurnValue.FEDAK_SARI:
                if (myPlayer.withCaller){
                    return null;
                } else {
                    return (
                        <div className={"act-option"} onClick={()=> act(Act.CONTRA)}>
                            {getContraString(TurnValue.KEREKES_BICIKLI)}
                        </div>
                    );
                }
            default: return null;
        }

        function getContraString(turnValue: TurnValue) : string {
            switch (turnValue + "") {
                case TurnValue.KONTRA: return "Kontra";
                case TurnValue.RE_KONTRA: return "Rekontra";
                case TurnValue.SZUB_KONTRA: return "Szubkontra";
                case TurnValue.MORD_KONTRA: return "Mordkontra";
                case TurnValue.HIRSCH_KONTRA: return "Hirschkontra";
                case TurnValue.FEDAK_SARI: return "Fedák Sári";
                case TurnValue.KEREKES_BICIKLI: return "Kerekes bicikli";
                default: return "";
            }
        }
    }

    function LastRound () {
        return(
            <div className={"last-round"}>
                {lastRound!.length > 0 && <Card card={lastRound![0]}/>}
                {lastRound!.length > 1 && <Card card={lastRound![1]}/>}
                {lastRound!.length > 2 && <Card card={lastRound![2]}/>}
                {lastRound!.length > 3 && <Card card={lastRound![3]}/>}
            </div>
        );
    }

    function Round () {
        return(
            <div className={"round"}>
                {round!.length > 0 && <Card card={round![0]}/>}
                {round!.length > 1 && <Card card={round![1]}/>}
                {round!.length > 2 && <Card card={round![2]}/>}
                {round!.length > 3 && <Card card={round![3]}/>}
            </div>
        );
    }

    function Csap () {
        return(
            <div className={"csap" + ((myTurn && gameStatus === GameStatus.CALL_CARD) ? " my-turn" : "")} >
                <Card card={csapCards![0]} onClick={gameStatus === GameStatus.CALL_CARD ? ()=> csapCard(0) : undefined}/>
                <Card card={csapCards![1]} onClick={gameStatus === GameStatus.CALL_CARD ? ()=> csapCard(1) : undefined}/>
                <Card card={csapCards![2]} onClick={gameStatus === GameStatus.CALL_CARD ? ()=> csapCard(2) : undefined}/>
            </div>
        );
    }

    async function act(act: Act) {
        let snapszerMoveDTO = {
            act: act
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function playCard(card: Card) {
        let snapszerMoveDTO = {
            card: card
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function callCard(card: Card) {
        let snapszerMoveDTO = {
            calledCard: card
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function callFigure(figure: Figure) {
        let snapszerMoveDTO = {
            csapFigure: figure
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function csapCard(index: number) {
        let snapszerMoveDTO = {
            csapIndex: index
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    function CalledCard () {
        if (!calledCard){
            return null;
        }

        function getCardName(card: Card) {
            function getColorName(color: Color) {
                switch (color) {
                    case Color.ZOLD: return "zöld";
                    case Color.KOR: return "kör";
                    case Color.TOK: return "tök";
                    case Color.MAKK: return "makk";
                    default: return "";
                }
            }

            function getFigureName(figure: Figure) {
                switch (figure) {
                    case Figure.ASZ: return "ász";
                    case Figure.TIZ: return "tíz";
                    case Figure.KIRALY: return "király";
                    case Figure.FELSO: return "felső";
                    case Figure.KILENC: return "kilenc";
                    default: return "";
                }
            }

            return getColorName(card.color) + " " + getFigureName(card.figure);
        }

        function getTurnValueString(turnValue: TurnValue) {
            switch (turnValue) {
                case TurnValue.BASIC: return "";
                case TurnValue.KONTRA: return " kontra";
                case TurnValue.RE_KONTRA: return " rekontra";
                case TurnValue.SZUB_KONTRA: return " szubkontra";
                case TurnValue.MORD_KONTRA: return " mordkontra";
                case TurnValue.HIRSCH_KONTRA: return " hirschkontra";
                case TurnValue.FEDAK_SARI: return " Fedák Sári";
                case TurnValue.KEREKES_BICIKLI: return " kerekes bicikli";
                default: return "";
            }
        }

        function getSnapszerString(snapszer: boolean) {
            return snapszer ? " snapszer" : "";
        }

        return(
            <div className={"called-card"}>
                {caller!.user.name + ": " + getCardName(calledCard) + getTurnValueString(turnValue!) + getSnapszerString(snapszer!)}
            </div>
        );


    }

    function WoundRounds () {
        return(
            <>
                {showPlayerWonCards &&
                <Modal isOpen={!!showPlayerWonCards} closeOnBackGroundClick={true} close={()=> {setShowPlayerWonCards(undefined)}}>
                    <div className={"won-rounds"} >
                    {showPlayerWonCards.wonRounds && showPlayerWonCards.wonRounds.map((woundRound, key) =>
                        <div key={key} className={"row"}>
                            {woundRound.map((card, key)=>
                                <Card key={key} card={card}/>
                            )}
                        </div>
                    )}
                    </div>
                </Modal>
                }
            </>
        );
    }

    function Count () {
        return(
            <div className={"count"} onClick={()=>count()}>
                <HandSVG/>
            </div>
        );
    }

    async function count() {
        let snapszerMoveDTO = {
            count: true
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    function MyHand () {
        return(
            <div className={"my-hand"} >
                {myPlayer.cards.map((card, key)=>
                    <div key={key} className={"card card-" + myPlayer.cards.length + "-" + (key+1)} onClick={()=> myTurn && gameStatus === GameStatus.PLAY_CARD && playCard(card)}>
                        <Card key={key} card={card}/>
                    </div>
                )}
            </div>
        );
    }

    type CardProps = {
        card: Card
        onClick?: ()=> any
    }

    function Card({card, onClick}: CardProps) {
        switch (card.color) {
            case Color.MAKK:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img alt = "makkasz" src={makkasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img alt = "makktiz" src={makktiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img alt = "makkkiraly" src={makkkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img alt = "makkfelso" src={makkfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img alt = "makkalso" src={makkalso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img alt = "makkkilenc" src={makkkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.TOK:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img alt = "tokasz" src={tokasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img alt = "toktiz" src={toktiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img alt = "tokkiraly" src={tokkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img alt = "tokfelso" src={tokfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img alt = "tokalso" src={tokalso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img alt = "tokkilenc" src={tokkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.KOR:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img alt = "korasz" src={korasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img alt = "kortiz" src={kortiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img alt = "korkiraly" src={korkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img alt = "korfelso" src={korfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img alt = "koralso" src={koralso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img alt = "korkilenc" src={korkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.ZOLD:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img alt = "zoldasz" src={zoldasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img alt = "zoldtiz" src={zoldtiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img alt = "zoldkiraly" src={zoldkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img alt = "zoldfelso" src={zoldfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img alt = "zoldalso" src={zoldalso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img alt = "zoldkilenc" src={zoldkilenc} onClick={()=> onClick && onClick()}/>;
                }
            default: return <img alt = "cardback" src={cardback} onClick={()=> onClick && onClick()}/>;
        }
    }


    function Game () {
        return(
            <div className={"game"}>
                <Board/>
                <PlayerBoard/>
            </div>
        );
    }


    function PlayerBoard () {
        return (
            <div className={"player-board"}>
                <table>
                    <tbody>
                    {players.map((player, key)=>
                        <PlayerRow key = {key} player={player}/>
                    )}
                    </tbody>
                </table>
            </div>
        );
    }

    type PlayerRowProps = {
        player: Player
    }

    function PlayerRow ({player}: PlayerRowProps) {
        const isNextPlayer = snapszerGameDTO.nextPlayer && snapszerGameDTO.nextPlayer.user.id === player.user.id === true;
        return(
            <tr className={"player-row" + (isNextPlayer ? " next" : "")}>
                <td onClick={()=> gameStatus === GameStatus.PLAY_CARD && setShowPlayerWonCards(player)} className={gameStatus === GameStatus.PLAY_CARD ? "pointer" : ""}>
                    <div className={"player-name"}>
                        {player.user.name}
                    </div>
                </td>
                <td>
                    <div className={"player-points"}>
                        {player.points}
                    </div>
                </td>
            </tr>
        );
    }

    function End () {
        return (
                <>
                    <Game/>
                </>
            );
    }

    return(
        <div className={"snapszer"}>
            <Content />
        </div>
    );

}


