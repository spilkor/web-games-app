import React, {useContext, useState} from 'react';


import {GameState} from "../../util/types";
import './snapszer.scss';
import {QuitButton, StartGameButton} from "../Game";
import {AppContext} from "../../App";
import {
    ActionType,
    Card,
    Color,
    Figure,
    Licit,
    Player,
    Round,
    RoundState,
    SnapszerGameDTO,
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

import twenty from "./png/twenty.png";
import forty from "./png/forty.png";

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
import {ReactComponent as ScoreBoardSVG} from './svg/scoreboardlogo.svg';

export function Snapszer () {

    const { gameData, user, gameSettings, setGameSettings  } = useContext(AppContext);

    const snapszerGameDTO = JSON.parse(gameData!.gameJSON) as SnapszerGameDTO;

    const {
        players,
        nextPlayer,
        round,
        rounds,
        scoreBoard
    } = snapszerGameDTO;


    const myTurn = nextPlayer && nextPlayer.user.id == user!.id;

    const myPlayer = players.find(player => player.user.id === user!.id) as Player;
    const myIndex = players.indexOf(myPlayer);

    const leftPlayer = players[getIndex(myIndex+1)];
    const oppositePlayer = players[getIndex(myIndex+2)];
    const rightPlayer = players[getIndex(myIndex+3)];

    const [jokerOpen, setJokerOpen] = useState<boolean>(false);

    const [scoreBoardOpen, setScoreBoardOpen] = useState<boolean>(false);

    const [showPlayerWonCards, setShowPlayerWonCards] = useState<Player>();

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
                {round && round.turn && <Round/>}
                {/*{lastRound && <LastRound/>}*/}
                {(round && round.roundState === RoundState.CALL_CARD || round && round.roundState === RoundState.CALL_FIGURE) &&
                    <Csap/>
                }
                {myTurn && (round && round.roundState === RoundState.CALL_CARD || round && round.roundState === RoundState.CALL_FIGURE) &&
                    <Joker/>
                }
                {myTurn && (round && round.roundState === RoundState.LICIT) &&
                <ActComp/>
                }
                <MyHand/>
                <WoundRounds/>
                <CalledCard/>
                {myTurn && round?.roundState === RoundState.TURNS && round.turns?.length !== 1 && round.turn?.cards?.length === 0 &&
                <Stop/>}

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

    function ScoreBoardLogo(){
        return(
            <div className={"score-board-logo" + (scoreBoardOpen ? " open" : "")} onClick={()=> setScoreBoardOpen(true)}>
                <ScoreBoardSVG/>
            </div>
        );
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
                    {round && round.roundState === RoundState.CALL_CARD ? <CallCard/> : <CallFigure/>}
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
            const csapColor = round && round.csapCard && round.csapCard.color;
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
                <div className={"act-option"} onClick={()=> act(Licit.CHECK)}>
                    Check
                </div>
                {(myPlayer.withCaller === true && round && !round.snapszer && round.firstLicitTurn) &&
                <div className={"act-option"} onClick={()=> act(Licit.SNAPSZER)}>
                    Snapszer
                </div>
                }
                <Contra/>
                <ContraSnapszer/>
                {round && round.firstLicitTurn && round.caller.user.id === myPlayer.user.id &&
                <div className={"act-option"} onClick={()=> act(Licit.THROW_IN)}>
                    Throw in
                </div>
                }
                {round && round.firstLicitTurn && round.caller.user.id === myPlayer.user.id && myPlayer.cards.filter(c => c.figure === Figure.KILENC).length >= 3 &&
                <div className={"act-option"} onClick={()=> act(Licit.THREE_NINE)}>
                    Three nine
                </div>
                }
            </div>
        );
    }

    function ContraSnapszer () {
        if (!(myPlayer.withCaller === true && round && !round.snapszer && round.firstLicitTurn)){
            return null;
        }
        switch (round && round.turnValue + "") {
            case TurnValue.KONTRA:
                return (
                    <div className={"act-option"} onClick={()=> act(Licit.CONTRA_SNAPSZER)}>
                        {getContraString(TurnValue.RE_KONTRA) + " snapszer"}
                    </div>
                );
            case TurnValue.SZUB_KONTRA:
                return (
                    <div className={"act-option"} onClick={()=> act(Licit.CONTRA_SNAPSZER)}>
                        {getContraString(TurnValue.MORD_KONTRA) + " snapszer"}
                    </div>
                );
            case TurnValue.HIRSCH_KONTRA:
                return (
                    <div className={"act-option"} onClick={()=> act(Licit.CONTRA_SNAPSZER)}>
                        {getContraString(TurnValue.FEDAK_SARI) + " snapszer"}
                    </div>
                );
            default: return null;
        }
    }

    function Contra () {
        switch (round && round.turnValue + "") {
            case TurnValue.BASIC:
                if (myPlayer.withCaller){
                    return null;
                } else {
                    return (
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
                            {getContraString(TurnValue.KONTRA)}
                        </div>
                    );
                }
            case TurnValue.KONTRA:
                if (myPlayer.withCaller){
                    return (
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
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
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
                            {getContraString(TurnValue.SZUB_KONTRA)}
                        </div>
                    );
                }
            case TurnValue.SZUB_KONTRA:
                if (myPlayer.withCaller){
                    return (
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
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
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
                            {getContraString(TurnValue.HIRSCH_KONTRA)}
                        </div>
                    );
                }
            case TurnValue.HIRSCH_KONTRA:
                if (myPlayer.withCaller){
                    return (
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
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
                        <div className={"act-option"} onClick={()=> act(Licit.CONTRA)}>
                            {getContraString(TurnValue.KEREKES_BICIKLI)}
                        </div>
                    );
                }
            default: return null;
        }
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

    function Round () {
        const myKopp = myTurn && round!.turn!.ended;
        return(
            <div className={"round" + (myKopp ? " kopp" : "")}>
                {round!.turn!.cards!.length > 0 && <Card card={round!.turn!.cards![0]} onClick={()=> myTurn && kopp()}/>}
                {round!.turn!.cards!.length > 1 && <Card card={round!.turn!.cards![1]} onClick={()=> myTurn && kopp()}/>}
                {round!.turn!.cards!.length > 2 && <Card card={round!.turn!.cards![2]} onClick={()=> myTurn && kopp()}/>}
                {round!.turn!.cards!.length > 3 && <Card card={round!.turn!.cards![3]} onClick={()=> myTurn && kopp()}/>}
                {round!.turn!.twenty && <Card className={"twenty-or-forty"} card={{color: Color.UNKNOWN, figure: Figure.TWENTY} as Card} onClick={()=> myTurn && kopp()}/>}
                {round!.turn!.forty && <Card className={"twenty-or-forty"} card={{color: Color.UNKNOWN, figure: Figure.FORTY} as Card} onClick={()=> myTurn && kopp()}/>}
            </div>
        );
    }

    async function kopp() {
        let snapszerMoveDTO = {
            actionType: ActionType.KOPP
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    function Csap () {
        return(
            <div className={"csap" + ((myTurn && round && round.roundState == RoundState.CALL_CARD) ? " my-turn" : "")} >
                <Card card={round!.csapCards![0]} onClick={round && round.roundState == RoundState.CALL_CARD ? ()=> csapCard(0) : undefined}/>
                <Card card={round!.csapCards![1]} onClick={round && round.roundState == RoundState.CALL_CARD ? ()=> csapCard(1) : undefined}/>
                <Card card={round!.csapCards![2]} onClick={round && round.roundState == RoundState.CALL_CARD ? ()=> csapCard(2) : undefined}/>
            </div>
        );
    }

    async function act(licit: Licit) {
        let snapszerMoveDTO = {
            licit
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function playCard(card: Card) {
        let snapszerMoveDTO;
        if (card.figure === Figure.TWENTY){
            snapszerMoveDTO = {
                actionType: ActionType.TWENTY,
            } as SnapszerMoveDTO;
            API.move(JSON.stringify(snapszerMoveDTO));
        } else if (card.figure === Figure.FORTY){
            snapszerMoveDTO = {
                actionType: ActionType.FORTY,
            } as SnapszerMoveDTO;
            API.move(JSON.stringify(snapszerMoveDTO));
        } else {
            snapszerMoveDTO = {
                actionType: ActionType.PLAY_CARD,
                card: card
            } as SnapszerMoveDTO;
        }
        API.move(JSON.stringify(snapszerMoveDTO));
    }


    async function callTwenty() {
        let snapszerMoveDTO = {
            actionType: ActionType.TWENTY
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function callForty() {
        let snapszerMoveDTO = {
            actionType: ActionType.FORTY
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    async function callCard(card: Card) {
        let snapszerMoveDTO = {
            card
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
        if (!round || !round.calledCard){
            return null;
        }
        return(
            <div className={"called-card"}>
                {round.caller.user.name + ": " + getRoundString(round)}
            </div>
        );
    }

    function getRoundString(round: Round) {
        if (!round || !round.calledCard){
            return "?";
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
                    case Figure.ALSO: return "alsó";
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

        return getCardName(round.calledCard) + getTurnValueString(round.turnValue!) + getSnapszerString(round.snapszer!);
    }

    function WoundRounds () {
        if (showPlayerWonCards){
            return (
                <Modal isOpen={!!showPlayerWonCards} closeOnBackGroundClick={true} close={()=> {setShowPlayerWonCards(undefined)}}>
                    <div className={"won-rounds"}>
                        {round && round.turns && round.turns.filter(turn => turn.ended === true && turn.strongestPlayer && turn.strongestPlayer.user.id === showPlayerWonCards.user.id).map((turn, key) =>
                            <div key={key} className={"row"}>
                                {turn.cards && turn.cards.map((card, key)=>
                                    <Card key={key} card={card}/>
                                )}
                            </div>
                        )}
                    </div>
                </Modal>
            );
        } else {
            return null;
        }
    }

    function Stop () {
        return(
            <div className={"stop"} onClick={()=>stop()}>
                <HandSVG/>
            </div>
        );
    }

    async function stop() {
        let snapszerMoveDTO = {
            actionType: ActionType.STOP
        } as SnapszerMoveDTO;
        API.move(JSON.stringify(snapszerMoveDTO));
    }

    function MyHand () {
        return(
            <div className={"my-hand"} >
                {myPlayer.cards.map((card, key)=>
                    <div key={key}
                         className={"card card-" + myPlayer.cards.length + "-" + (key+1)}
                         onClick={()=> myTurn && round && round.roundState === RoundState.TURNS && playCard(card)}>
                        <Card key={key} card={card}/>
                    </div>
                )}
            </div>
        );
    }

    type CardProps = {
        card: Card
        onClick?: ()=> any
        className?: string
    }

    function Card({card, onClick, className}: CardProps) {
        switch (card.color) {
            case Color.MAKK:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img className={className} alt = "makkasz" src={makkasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img className={className} alt = "makktiz" src={makktiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img className={className} alt = "makkkiraly" src={makkkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img className={className} alt = "makkfelso" src={makkfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img className={className} alt = "makkalso" src={makkalso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img className={className} alt = "makkkilenc" src={makkkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.TOK:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img className={className} alt = "tokasz" src={tokasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img className={className} alt = "toktiz" src={toktiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img className={className} alt = "tokkiraly" src={tokkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img className={className} alt = "tokfelso" src={tokfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img className={className} alt = "tokalso" src={tokalso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img className={className} alt = "tokkilenc" src={tokkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.KOR:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img className={className} alt = "korasz" src={korasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img className={className} alt = "kortiz" src={kortiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img className={className} alt = "korkiraly" src={korkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img className={className} alt = "korfelso" src={korfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img className={className} alt = "koralso" src={koralso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img className={className} alt = "korkilenc" src={korkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.ZOLD:
                switch (card.figure) {
                    case Figure.ASZ:
                        return <img className={className} alt = "zoldasz" src={zoldasz} onClick={()=> onClick && onClick()}/>;
                    case Figure.TIZ:
                        return <img className={className} alt = "zoldtiz" src={zoldtiz} onClick={()=> onClick && onClick()}/>;
                    case Figure.KIRALY:
                        return <img className={className} alt = "zoldkiraly" src={zoldkiraly} onClick={()=> onClick && onClick()}/>;
                    case Figure.FELSO:
                        return <img className={className} alt = "zoldfelso" src={zoldfelso} onClick={()=> onClick && onClick()}/>;
                    case Figure.ALSO:
                        return <img className={className} alt = "zoldalso" src={zoldalso} onClick={()=> onClick && onClick()}/>;
                    case Figure.KILENC:
                        return <img className={className} alt = "zoldkilenc" src={zoldkilenc} onClick={()=> onClick && onClick()}/>;
                }
            case Color.UNKNOWN:
                switch (card.figure) {
                    case Figure.TWENTY:
                        return <img className={className} alt = "twenty" src={twenty} onClick={()=> onClick && onClick()}/>;
                    case Figure.FORTY:
                        return <img className={className} alt = "forty" src={forty} onClick={()=> onClick && onClick()}/>;
                }
            default: return <img className={className} alt = "cardback" src={cardback} onClick={()=> onClick && onClick()}/>;
        }
    }


    function Game () {
        return(
            <div className={"game"}>
                <Board/>
                <PlayerBoard/>
                {scoreBoardOpen && <ScoreBoardModal/>}
            </div>
        );
    }

    function End () {
        return (
            <>
                <ScoreBoard/>
                {gameData?.owner.id === user!.id && <QuitButton/>}
            </>
        );
    }

    type PlayerRowProps = {
        player: Player
    }

    function PlayerRow ({player}: PlayerRowProps) {
        const isNextPlayer = snapszerGameDTO.nextPlayer && snapszerGameDTO.nextPlayer.user.id === player.user.id === true;
        return(
            <tr className={"player-row" + (isNextPlayer ? " next" : "")}>
                <td onClick={()=> round && round.roundState == RoundState.TURNS && setShowPlayerWonCards(player)} className={round && round.roundState == RoundState.TURNS ? "pointer" : ""}>
                    <div className={"player-name"}>
                        {player.user.name}
                    </div>
                </td>
                <td>
                    <div className={"player-points"}>
                        {scoreBoard[players.indexOf(player)]}
                    </div>
                </td>
            </tr>
        );
    }

    function PlayerBoard () {
        if (scoreBoardOpen){
            return null;
        }
        return (
            <div className={"player-board"}>
                <table>
                    <tbody>
                    {players.map((player, key)=>
                        <PlayerRow key = {key} player={player}/>
                    )}
                    </tbody>
                </table>

                <ScoreBoardLogo/>
            </div>
        );
    }

    function ScoreBoardModal () {
        return(
            <Modal isOpen={scoreBoardOpen} closeOnBackGroundClick={true} close={()=> {setScoreBoardOpen(false)}}>
                <ScoreBoard/>
            </Modal>
        );
    }

    function ScoreBoard () {
        return(
            <div className={"score-board"}>
                <div className={"score-board-row players"}>
                    <div className={"score-board-cell border-none is-wide"}>
                    </div>
                    {players.map((player, key)=>
                        <div key = {key} className={"score-board-cell is-bold"}>{player.user.name}</div>
                    )}
                </div>

                <div className={"score-board-wrapper"}>
                    {rounds && rounds.map((round, key)=>
                        <div key={key} className={"score-board-row"}>
                            <div className={"score-board-cell border-none is-wide c-first-letter"}>
                                {getRoundString(round)}
                            </div>
                            {players.map((player, key2)=>
                                <div className={"score-board-cell" + (round.caller.user.id === player.user.id ? " caller" : "")} key = {key2}>{round.scoreBoard === null ? "?" : round.scoreBoard[players.indexOf(player)]}</div>
                            )}
                        </div>
                    )}
                </div>

                <div className={"score-board-row"}>
                    <div className={"score-board-cell is-bold is-wide"}>
                        VICTORY POINTS
                    </div>
                    {players.map((player, key)=>
                        <div key = {key} className={"score-board-cell is-bold"}>{scoreBoard[players.indexOf(player)]}</div>
                    )}
                </div>
            </div>
        );
    }

    return(
        <div className={"snapszer"}>
            <Content />
        </div>
    );

}


