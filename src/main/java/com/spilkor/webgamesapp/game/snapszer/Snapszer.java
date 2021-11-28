package com.spilkor.webgamesapp.game.snapszer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Sets;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.game.snapszer.dto.PlayerDTO;
import com.spilkor.webgamesapp.game.snapszer.dto.SnapszerGameDTO;
import com.spilkor.webgamesapp.game.snapszer.dto.SnapszerMoveDTO;
import com.spilkor.webgamesapp.game.snapszer.enums.*;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;

import java.util.*;
import java.util.stream.Collectors;


public class Snapszer extends Game {

    private List<Player> players = new ArrayList<>();
    private Player caller = null;
    private Player nextPlayer = null;
    private GameStatus gameStatus = null;
    private List<Card> csapCards = null;
    private Integer csapIndex;
    private Color adu;
    private Card calledCard;
    private Boolean snapszer;
    private TurnValue turnValue;
    private Player lastTurnValueModifier;
    private Card firstRoundCard;
    private LinkedHashMap<Card, Player> round;
    private Card strongestRoundCard;
    private Boolean secret;
    private List<Card> lastRound = null;

    public Snapszer(UserDTO owner, GameType gameType){
        super(owner, gameType);
        players.add(new Player(owner));
    }

    @Override
    public void playerJoined(UserDTO user) {
        players.add(new Player(user));
    }

    @Override
    public void playerLeft(UserDTO user) {
        players = players.stream().filter(player -> !player.getUser().equals(user)).collect(Collectors.toList());
    }

    private Player getPlayer(UserDTO user){
        return players.stream().filter(player -> player.getUser().equals(user)).findFirst().orElse(null);
    }

    @Override
    public void surrender(UserDTO userDTO) {
//        TODO
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        return false;
    }

    @Override
    public boolean isStartable() {
        return players.size() == 4;
//        return players.size() == 4; FIXME
    }

    @Override
    public void start() {
        gameStatus = GameStatus.CALL_CARD;

        Collections.shuffle(players);
        players.forEach(player -> player.setPoints(0));

        caller = players.get(0);
        nextPlayer = caller;

        dealCards();

        round = new LinkedHashMap<>();
        snapszer = false;
        secret = true;
    }

    private void dealCards(){
        Player player_1 = getNextPlayer(caller);
        Player player_2 = getNextPlayer(player_1);
        Player player_3 = getNextPlayer(player_2);

        csapCards = new ArrayList<>();

        Deck deck = new Deck();

        caller.getCards().add(deck.draw());
        player_1.getCards().add(deck.draw());
        player_2.getCards().add(deck.draw());
        player_3.getCards().add(deck.draw());

        caller.getCards().add(deck.draw());
        player_1.getCards().add(deck.draw());
        player_2.getCards().add(deck.draw());
        player_3.getCards().add(deck.draw());

        caller.getCards().add(deck.draw());
        player_1.getCards().add(deck.draw());
        player_2.getCards().add(deck.draw());
        player_3.getCards().add(deck.draw());

        csapCards.add(deck.draw());
        player_1.getCards().add(deck.draw());
        player_2.getCards().add(deck.draw());
        player_3.getCards().add(deck.draw());

        csapCards.add(deck.draw());
        player_1.getCards().add(deck.draw());
        player_2.getCards().add(deck.draw());
        player_3.getCards().add(deck.draw());

        csapCards.add(deck.draw());
        player_1.getCards().add(deck.draw());
        player_2.getCards().add(deck.draw());
        player_3.getCards().add(deck.draw());
    }

    @Override
    public void restart() {
        gameStatus = null;
        caller = null;
        nextPlayer = null;
        players.forEach(player -> player.setCards(new ArrayList<>()));
        players.forEach(player -> player.setPoints(null));
        players.forEach(player -> player.setWithCaller(null));
        csapCards = null;
        csapIndex = null;
        adu = null;
        calledCard = null;
        snapszer = null;
        turnValue = null;
        lastTurnValueModifier = null;
        firstRoundCard = null;
        round = null;
        strongestRoundCard = null;
        secret = null;
        lastRound = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        SnapszerGameDTO snapszerGameDTO = new SnapszerGameDTO();
        List<PlayerDTO> playersDTOs = new ArrayList<>();

        PlayerDTO callerDTO = null;
        PlayerDTO nextPlayerDTO = null;
        for (Player player: players){
            PlayerDTO playerDTO = new PlayerDTO();
            playerDTO.setUser(player.getUser());
            playerDTO.setCards(player.getUser().equals(user) ? player.getCards() : player.getCards().stream().map(c-> new Card(Color.UNKNOWN, Figure.UNKNOWN)).collect(Collectors.toList()));
            playerDTO.setPoints(player.getPoints());
            playerDTO.setWithCaller(player.getUser().equals(user) ? player.getWithCaller() : null);

            if (player.getUser().equals(user) || (!Boolean.TRUE.equals(secret) && Boolean.TRUE.equals(getPlayer(user).getWithCaller()) == Boolean.TRUE.equals(player.getWithCaller()))){
                playerDTO.setWonRounds(player.getWonRounds());
            } else {
                player.getWonRounds().forEach(wr -> playerDTO.getWonRounds().add(wr.stream().map(c-> wr == lastRound ? c : new Card(Color.UNKNOWN, Figure.UNKNOWN)).collect(Collectors.toList())));
            }

            if (caller != null && player.getUser().equals(caller.getUser())){
                callerDTO = playerDTO;
            }
            if (nextPlayer != null && player.getUser().equals(nextPlayer.getUser())){
                nextPlayerDTO = playerDTO;
            }

            playersDTOs.add(playerDTO);
        }
        snapszerGameDTO.setLastRound(lastRound);
        snapszerGameDTO.setPlayers(playersDTOs);
        snapszerGameDTO.setCaller(callerDTO);
        snapszerGameDTO.setGameStatus(gameStatus);
        snapszerGameDTO.setNextPlayer(nextPlayerDTO);

        if (csapCards != null){
            snapszerGameDTO.setCsapCards(csapCards.stream().map(csapCard -> csapIndex != null && csapIndex == csapCards.indexOf(csapCard) ? csapCard : new Card(Color.UNKNOWN, Figure.UNKNOWN)).collect(Collectors.toList()));
        }
        snapszerGameDTO.setCsapIndex(csapIndex);
        snapszerGameDTO.setAdu(adu);
        snapszerGameDTO.setSnapszer(snapszer);
        snapszerGameDTO.setTurnValue(turnValue);
        snapszerGameDTO.setCalledCard(calledCard);
        if (round != null){
            snapszerGameDTO.setRound(round.keySet());
        }

        try {
            return Mapper.writeValueAsString(snapszerGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
        if (nextPlayer == null || !nextPlayer.getUser().equals(userDTO)){
            return false;
        }
        try {
            SnapszerMoveDTO snapszerMoveDTO = Mapper.readValue(moveJSON, SnapszerMoveDTO.class);

            if(GameStatus.CALL_CARD.equals(gameStatus)){
                Integer csapIndex = snapszerMoveDTO.getCsapIndex();
                if (csapIndex != null){
                    return MathUtil.inRange(0, csapIndex, 3);
                } else {
                    Card calledCard = snapszerMoveDTO.getCalledCard();
                    return calledCard != null
                            && calledCard.getColor() != null && !Color.UNKNOWN.equals(calledCard.getColor())
                            && calledCard.getFigure() != null && !Figure.UNKNOWN.equals(calledCard.getFigure());
                }
            } else if (GameStatus.CALL_FIGURE.equals(gameStatus)){
                Figure csapFigure = snapszerMoveDTO.getCsapFigure();
                return csapFigure != null && !Figure.UNKNOWN.equals(csapFigure);
            } else if (GameStatus.FIRST_ACT.equals(gameStatus)){
                return Sets.newHashSet(Act.CHECK, Act.SNAPSZER, Act.THROW_IN).contains(snapszerMoveDTO.getAct());
            } else if (GameStatus.ACT.equals(gameStatus)){
                Act act = snapszerMoveDTO.getAct();
                if (act == null){
                    return false;
                }
                switch (act){
                    case CHECK:
                        return true;
                    case SNAPSZER:
                        return !snapszer && nextPlayer.getWithCaller();
                    case CONTRA:
                        if (nextPlayer.getWithCaller()){
                            return Sets.newHashSet(TurnValue.KONTRA, TurnValue.SZUB_KONTRA, TurnValue.HIRSCH_KONTRA).contains(turnValue);
                        } else {
                            return Sets.newHashSet(TurnValue.BASIC, TurnValue.RE_KONTRA, TurnValue.MORD_KONTRA, TurnValue.FEDAK_SARI).contains(turnValue);
                        }
                        default: return false;
                }
            } else if(GameStatus.PLAY_CARD.equals(gameStatus)){
                Card card = snapszerMoveDTO.getCard();

                if (card == null ||
                        card.getColor() == null || Color.UNKNOWN.equals(card.getColor()) ||
                        card.getFigure() == null || Figure.UNKNOWN.equals(card.getFigure())){
                    return false;
                }

                if (!nextPlayer.getCards().contains(card)){
                    return false;
                }

                if (firstRoundCard == null){
                    return true;
                } else {
                    if (nextPlayer.getCards().stream().map(Card::getColor).anyMatch(c -> c.equals(firstRoundCard.getColor()))){
                        if (!firstRoundCard.getColor().equals(adu) && strongestRoundCard.getColor().equals(adu)){
                            return card.getColor().equals(firstRoundCard.getColor());
                        } else {
                            if (nextPlayer.getCards().stream().filter(c-> c.getColor().equals(firstRoundCard.getColor())).anyMatch(c-> c.getFigure().strongerThan(strongestRoundCard.getFigure()))){
                                return card.getColor().equals(firstRoundCard.getColor()) && card.getFigure().strongerThan(strongestRoundCard.getFigure());
                            } else {
                                return card.getColor().equals(firstRoundCard.getColor());
                            }
                        }
                    } else if (nextPlayer.getCards().stream().map(Card::getColor).anyMatch(c -> c.equals(adu))){
                        if (strongestRoundCard.getColor().equals(adu)){
                            if (nextPlayer.getCards().stream().filter(c-> c.getColor().equals(adu)).anyMatch(c-> c.getFigure().strongerThan(strongestRoundCard.getFigure()))){
                                return card.getColor().equals(adu) && card.getFigure().strongerThan(strongestRoundCard.getFigure());
                            } else {
                                return card.getColor().equals(adu);
                            }
                        } else {
                            return card.getColor().equals(adu);
                        }
                    } else {
                        return true;
                    }
                }
            }
            return false;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            SnapszerMoveDTO snapszerMoveDTO = Mapper.readValue(moveJSON, SnapszerMoveDTO.class);

            if(GameStatus.CALL_CARD.equals(gameStatus)){
                Integer csapIndex = snapszerMoveDTO.getCsapIndex();
                if (csapIndex != null){
                    gameStatus = GameStatus.CALL_FIGURE;
                    this.csapIndex = csapIndex;
                } else {
                    calledCard = snapszerMoveDTO.getCalledCard();
                    adu = calledCard.getColor();
                    gameStatus = GameStatus.FIRST_ACT;
                    caller.getCards().addAll(csapCards);
                    csapCards = null;
                    for(Player player: players){
                        player.setWithCaller(caller.getUser().equals(player.getUser()) || player.getCards().contains(calledCard));
                    }
                    turnValue = TurnValue.BASIC;
                }
            } else if(GameStatus.CALL_FIGURE.equals(gameStatus)){
                Card csap = csapCards.get(csapIndex);
                Figure csapFigure = snapszerMoveDTO.getCsapFigure();
                adu = csap.getColor();
                calledCard = new Card(adu, csapFigure);
                gameStatus = GameStatus.FIRST_ACT;
                caller.getCards().addAll(csapCards);
                csapCards = null;
                csapIndex = null;
                for(Player player: players){
                    player.setWithCaller(caller == player || player.getCards().contains(calledCard));
                }
                turnValue = TurnValue.BASIC;
            } else if (GameStatus.FIRST_ACT.equals(gameStatus)){
                Act act = snapszerMoveDTO.getAct();
                if (Act.CHECK.equals(act)){
                    nextPlayer = getNextPlayer();
                    gameStatus = GameStatus.ACT;
                    lastTurnValueModifier = caller;
                } else if(Act.SNAPSZER.equals(act)){
                    snapszer = true;
                    nextPlayer = getNextPlayer();
                    gameStatus = GameStatus.ACT;
                    lastTurnValueModifier = caller;
                } else if(Act.THROW_IN.equals(act)){
                    endRound();
                }
            } else if (GameStatus.ACT.equals(gameStatus)){
                Act act = snapszerMoveDTO.getAct();
                switch (act){
                    case CHECK:
                        Player nextP = getNextPlayer();
                        if (nextP == lastTurnValueModifier){
                            nextPlayer = caller;
                            gameStatus = GameStatus.PLAY_CARD;
                            lastTurnValueModifier = null;
                        } else {
                            nextPlayer = nextP;
                        }
                        return;
                    case SNAPSZER:
                        snapszer = true;
                        lastTurnValueModifier = this.nextPlayer;
                        nextPlayer = getNextPlayer();
                        return;
                    case CONTRA:
                        lastTurnValueModifier = nextPlayer;
                        nextPlayer = getNextPlayer();
                        switch (turnValue){
                            case BASIC:
                                turnValue = TurnValue.KONTRA;
                                return;
                            case KONTRA:
                                turnValue = TurnValue.RE_KONTRA;
                                return;
                            case RE_KONTRA:
                                turnValue = TurnValue.SZUB_KONTRA;
                                return;
                            case SZUB_KONTRA:
                                turnValue = TurnValue.MORD_KONTRA;
                                return;
                            case MORD_KONTRA:
                                turnValue = TurnValue.HIRSCH_KONTRA;
                                return;
                            case HIRSCH_KONTRA:
                                turnValue = TurnValue.FEDAK_SARI;
                                return;
                            case FEDAK_SARI:
                                turnValue = TurnValue.KEREKES_BICIKLI;
                        }
                }
            } else if (GameStatus.PLAY_CARD.equals(gameStatus)){
                Card card = snapszerMoveDTO.getCard();
                nextPlayer.getCards().remove(card);
                round.put(card, nextPlayer);
                if (card.equals(calledCard)){
                    secret = false;
                }

                if (round.size() == 1){
                    nextPlayer = getNextPlayer();
                    firstRoundCard = card;
                    strongestRoundCard = card;
                } else if (round.size() == 4){

                    if (strongestRoundCard.getColor().equals(adu)){
                        if (card.getColor().equals(adu) && card.getFigure().strongerThan(strongestRoundCard.getFigure())){
                            strongestRoundCard = card;
                        }
                    } else if (card.getColor().equals(adu)){
                        strongestRoundCard = card;
                    } else if (strongestRoundCard.getColor().equals(card.getColor()) && card.getFigure().strongerThan(strongestRoundCard.getFigure())){
                        strongestRoundCard = card;
                    }

                    Player roundWinner = round.get(strongestRoundCard);

                    lastRound = new ArrayList<>(round.keySet());
                    roundWinner.getWonRounds().add(lastRound);

                    if (roundWinner.getCards().isEmpty()){
                        Player winner = round.get(strongestRoundCard);
                        boolean callerWon = winner.getWithCaller();




                        //...ha nincs vége
                        gameStatus = GameStatus.CALL_CARD;
                        nextPlayer = getNextPlayer(caller);
                        strongestRoundCard = null;
                        firstRoundCard = null;
                        round.clear();
                        adu = null;
                        snapszer = false;
                        calledCard = null;
                        turnValue = TurnValue.BASIC;
                        caller = nextPlayer;
                        secret = true;
                        lastRound = null;
                        players.forEach(p-> p.getWonRounds().clear());
                        players.forEach(p-> p.setWithCaller(null));
                        dealCards();
                    } else {
                        nextPlayer = roundWinner;
                        strongestRoundCard = null;
                        firstRoundCard = null;
                        round.clear();
                    }
                } else {
                    nextPlayer = getNextPlayer();
                    if (strongestRoundCard.getColor().equals(adu)){
                        if (card.getColor().equals(adu) && card.getFigure().strongerThan(strongestRoundCard.getFigure())){
                            strongestRoundCard = card;
                        }
                    } else if (card.getColor().equals(adu)){
                        strongestRoundCard = card;
                    } else if (strongestRoundCard.getColor().equals(card.getColor()) && card.getFigure().strongerThan(strongestRoundCard.getFigure())){
                        strongestRoundCard = card;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void endRound() {
        //TODO
    }

    private Player getNextPlayer(Player player) {
        return players.get((players.indexOf(player) + 1) % players.size());
    }

    private Player getNextPlayer() {
        return players.get((players.indexOf(nextPlayer) + 1) % players.size());
    }

}
