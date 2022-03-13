package com.spilkor.webgamesapp.game.snapszer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.game.snapszer.dto.*;
import com.spilkor.webgamesapp.game.snapszer.enums.*;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;

import java.util.*;
import java.util.stream.Collectors;


public class Snapszer extends Game {

    private List<Player> players = new ArrayList<>();
    private Player nextPlayer = null;
    private Round round = null;
    private ScoreBoard scoreBoard;
    private List<Round> rounds = new ArrayList<>();
    private final Card twenty = new Card(Color.UNKNOWN, Figure.TWENTY);
    private final Card forty = new Card(Color.UNKNOWN, Figure.FORTY);
    private final Card unknownCard = new Card(Color.UNKNOWN, Figure.UNKNOWN);
    private final Set<Figure> unPlayableFigures = Sets.newHashSet(Figure.UNKNOWN, Figure.TWENTY, Figure.FORTY);

    public Snapszer(UserDTO owner, GameType gameType){
        super(owner, gameType);
        Player player = new Player(owner);
        players.add(player);
        scoreBoard = new ScoreBoard(player);
    }

    @Override
    public void playerJoined(UserDTO user) {
        Player player = new Player(user);
        players.add(player);
        scoreBoard.addPlayer(player);
    }

    @Override
    public void playerLeft(UserDTO user) {
        Player player = getPlayer(user);
        players.remove(player);
        scoreBoard.removePlayer(player);
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        return false;
    }

    @Override
    public boolean isStartable() {
        return players.size() == 4;
    }

    @Override
    public void start() {
        Collections.shuffle(players);
        nextPlayer = players.get(0);
        round = new Round(nextPlayer);
        rounds.add(round);
    }

    @Override
    public String getGameJSON(UserDTO user) {
        SnapszerGameDTO snapszerGameDTO = new SnapszerGameDTO();

        List<PlayerDTO> playerDTOs = new ArrayList<>();
        for(Player player: players){
            PlayerDTO playerDTO = new PlayerDTO();

            playerDTO.setUser(player.getUser());
            if (player.getUser().equals(user)){
                if (nextPlayer == getPlayer(user)
                        && round != null
                        && RoundState.TURNS.equals(round.roundState)
                        && nextPlayer == getPlayer(user)
                        && round.turn.cards.isEmpty()
                        && !round.turn.twenty
                        && !round.turn.forty){
                    if (nextPlayer.getCards().stream()
                            .filter(card -> !card.getColor().equals(round.adu))
                            .filter(card -> Figure.KIRALY.equals(card.getFigure()))
                            .anyMatch(card -> nextPlayer.getCards().stream()
                                    .anyMatch(c -> c.getColor().equals(card.getColor()) && Figure.FELSO.equals(c.getFigure())))){
                        playerDTO.getCards().add(twenty);
                    }
                    if (nextPlayer.getCards().stream()
                            .filter(card -> card.getColor().equals(round.adu))
                            .filter(card -> Figure.KIRALY.equals(card.getFigure()))
                            .anyMatch(card -> nextPlayer.getCards().stream()
                                    .anyMatch(c -> c.getColor().equals(card.getColor()) && Figure.FELSO.equals(c.getFigure())))){
                        playerDTO.getCards().add(forty);
                    }
                }
                playerDTO.getCards().addAll(player.getCards());
            } else {
                playerDTO.setCards(player.getCards().stream().map(c-> unknownCard).collect(Collectors.toList()));
            }
            playerDTO.setWithCaller(!player.getUser().equals(user) || round == null ? null : round.playersWithCaller.contains(getPlayer(user)));

            playerDTOs.add(playerDTO);

            if (player.equals(nextPlayer)){
                snapszerGameDTO.setNextPlayer(playerDTO);
            }
        }
        snapszerGameDTO.setPlayers(playerDTOs);

        List<RoundDTO> roundDTOs = new ArrayList<>();
        for(Round r: rounds){
            RoundDTO roundDTO = new RoundDTO();

            roundDTO.setCaller(playerDTOs.stream().filter(playerDTO -> playerDTO.getUser().equals(r.caller.getUser())).findFirst().orElse(null));
            roundDTO.setRoundState(r.roundState);
            roundDTO.setCsapCards(r.csapCards.stream().map(csapCard -> r.csapCard == csapCard ? csapCard : unknownCard).collect(Collectors.toList()));
            roundDTO.setCsapCard(r.csapCard);
            roundDTO.setCalledCard(r.calledCard);
            roundDTO.setAdu(r.adu);
            roundDTO.setSnapszer(r.snapszer);
            roundDTO.setTurnValue(r.turnValue);
            roundDTO.setFirstLicitTurn(r.firstLicitTurn);

            if (r.finalScoreBoard != null){
                roundDTO.setScoreBoard(r.finalScoreBoard.toIntegerList(players));
            }

            List<TurnDTO> turnDTOs = new ArrayList<>();
            for(Turn t: r.turns){
                TurnDTO turnDTO = new TurnDTO();

                turnDTO.setCaller(playerDTOs.stream().filter(playerDTO -> playerDTO.getUser().equals(t.caller.getUser())).findFirst().orElse(null));

                if (!t.ended){
                    turnDTO.setCards(t.cards);
                } else if(t.strongestPlayer != null && (user.equals(t.strongestPlayer.getUser()) || (!r.secret && r.playersWithCaller.contains(getPlayer(user)) == r.playersWithCaller.contains(t.strongestPlayer)))){
                    turnDTO.setCards(t.cards);
                } else if (t == r.getLastEndedTurn()){
                    turnDTO.setCards(t.cards);
                } else {
                    turnDTO.setCards(t.cards.stream().map(card -> unknownCard).collect(Collectors.toList()));
                }

                turnDTO.setTwenty(t.twenty);
                turnDTO.setForty(t.forty);
                turnDTO.setEnded(t.ended);
                turnDTO.setStrongestPlayer(t.strongestPlayer);

                turnDTOs.add(turnDTO);
                if (t == r.turn){
                    roundDTO.setTurn(turnDTO);
                }
            }
            roundDTO.setTurns(turnDTOs);

            roundDTOs.add(roundDTO);
            if (r == round){
                snapszerGameDTO.setRound(roundDTO);
            }
        }
        snapszerGameDTO.setRounds(roundDTOs);

        snapszerGameDTO.setScoreBoard(scoreBoard.toIntegerList(players));

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

            if(RoundState.CALL_CARD.equals(round.roundState)){
                Integer csapIndex = snapszerMoveDTO.getCsapIndex();
                if (csapIndex != null){
                    return MathUtil.inRange(0, csapIndex, 3);
                } else {
                    Card card = snapszerMoveDTO.getCard();
                    return card != null
                            && card.getColor() != null && !Color.UNKNOWN.equals(card.getColor())
                            && card.getFigure() != null && !unPlayableFigures.contains(card.getFigure());
                }

            } else if (RoundState.CALL_FIGURE.equals(round.roundState)){
                Figure csapFigure = snapszerMoveDTO.getCsapFigure();
                return csapFigure != null && !unPlayableFigures.contains(csapFigure);

            } else if (RoundState.LICIT.equals(round.roundState)){
                Licit licit = snapszerMoveDTO.getLicit();
                if (licit == null){
                    return false;
                }
                switch (licit){
                    case CHECK:
                        return true;

                    case SNAPSZER:
                        return round.playersWithCaller.contains(nextPlayer)
                                && !round.snapszer
                                && round.firstLicitTurn;

                    case CONTRA:
                        if (round.playersWithCaller.contains(nextPlayer)){
                            return Sets.newHashSet(TurnValue.KONTRA, TurnValue.SZUB_KONTRA, TurnValue.HIRSCH_KONTRA)
                                    .contains(round.turnValue);
                        } else {
                            return Sets.newHashSet(TurnValue.BASIC, TurnValue.RE_KONTRA, TurnValue.MORD_KONTRA, TurnValue.FEDAK_SARI)
                                    .contains(round.turnValue);
                        }

                    case CONTRA_SNAPSZER:
                        return round.playersWithCaller.contains(nextPlayer)
                                && !round.snapszer
                                && round.firstLicitTurn
                                && Sets.newHashSet(TurnValue.KONTRA, TurnValue.SZUB_KONTRA, TurnValue.HIRSCH_KONTRA)
                                .contains(round.turnValue);

                    case THROW_IN:
                        return round.caller.equals(nextPlayer) && round.firstLicitTurn;

                    case THREE_NINE:
                        return round.caller.equals(nextPlayer)
                                && round.firstLicitTurn
                                && 3 <= nextPlayer.getCards().stream().filter(c-> Figure.KILENC.equals(c.getFigure())).count();
                    default: return false;
                }
            } else if (RoundState.TURNS.equals(round.roundState)){
                ActionType actionType = snapszerMoveDTO.getActionType();
                if (actionType == null){
                    return false;
                }
                switch (actionType){
                    case TWENTY:
                        return round.turn.cards.isEmpty() && !round.turn.twenty && !round.turn.forty
                                && nextPlayer.getCards().stream()
                                .filter(card -> !card.getColor().equals(round.adu))
                                .filter(card -> Figure.KIRALY.equals(card.getFigure()))
                                .anyMatch(card -> nextPlayer.getCards().stream()
                                        .anyMatch(c -> c.getColor().equals(card.getColor()) && Figure.FELSO.equals(c.getFigure())));
                    case FORTY:
                        return round.turn.cards.isEmpty() && !round.turn.twenty && !round.turn.forty
                                && nextPlayer.getCards().stream()
                                .filter(card -> card.getColor().equals(round.adu))
                                .filter(card -> Figure.KIRALY.equals(card.getFigure()))
                                .anyMatch(card -> nextPlayer.getCards().stream()
                                        .anyMatch(c -> c.getColor().equals(card.getColor()) && Figure.FELSO.equals(c.getFigure())));
                    case PLAY_CARD:
                        if (round.turn.ended){
                            return false;
                        }
                        Card card = snapszerMoveDTO.getCard();
                        if (card == null ||
                                card.getColor() == null || Color.UNKNOWN.equals(card.getColor()) ||
                                card.getFigure() == null || unPlayableFigures.contains(card.getFigure())){
                            return false;
                        }

                        if (!nextPlayer.getCards().contains(card)){
                            return false;
                        }

                        if (round.turn.cards.isEmpty()){
                            if (round.turn.twenty){
                                return !card.getColor().equals(round.adu) &&
                                        nextPlayer.getCards().stream()
                                                .anyMatch(c -> c.getColor().equals(card.getColor())
                                                        && (Figure.KIRALY.equals(card.getFigure()) && Figure.FELSO.equals(c.getFigure())
                                                        || Figure.KIRALY.equals(c.getFigure()) && Figure.FELSO.equals(card.getFigure())));
                            } else if (round.turn.forty){
                                return card.getColor().equals(round.adu) &&
                                        nextPlayer.getCards().stream()
                                                .anyMatch(c -> c.getColor().equals(card.getColor())
                                                        && (Figure.KIRALY.equals(card.getFigure()) && Figure.FELSO.equals(c.getFigure())
                                                        || Figure.KIRALY.equals(c.getFigure()) && Figure.FELSO.equals(card.getFigure())));
                            } else {
                                return true;
                            }
                        } else {
                            if (nextPlayer.getCards().stream().anyMatch(c -> c.getColor().equals(round.turn.firstCard.getColor()))){
                                if (!round.turn.firstCard.getColor().equals(round.adu) && round.turn.strongestCard.getColor().equals(round.adu)){
                                    return card.getColor().equals(round.turn.firstCard.getColor());
                                } else {
                                    if (nextPlayer.getCards().stream().filter(c-> c.getColor().equals(round.turn.firstCard.getColor())).anyMatch(c-> c.getFigure().strongerThan(round.turn.strongestCard.getFigure()))){
                                        return card.getColor().equals(round.turn.firstCard.getColor()) && card.getFigure().strongerThan(round.turn.strongestCard.getFigure());
                                    } else {
                                        return card.getColor().equals(round.turn.firstCard.getColor());
                                    }
                                }
                            } else if (nextPlayer.getCards().stream().anyMatch(c -> c.getColor().equals(round.adu))){
                                if (round.turn.strongestCard.getColor().equals(round.adu)){
                                    if (nextPlayer.getCards().stream().filter(c-> c.getColor().equals(round.adu)).anyMatch(c-> c.getFigure().strongerThan(round.turn.strongestCard.getFigure()))){
                                        return card.getColor().equals(round.adu) && card.getFigure().strongerThan(round.turn.strongestCard.getFigure());
                                    } else {
                                        return card.getColor().equals(round.adu);
                                    }
                                } else {
                                    return card.getColor().equals(round.adu);
                                }
                            } else {
                                return true;
                            }
                        }
                    case STOP:
                        if (round.turn.cards.isEmpty() && (round.caller.equals(nextPlayer) || !round.secret)){
                            Set<Player> countForPlayers = Sets.newHashSet();
                            if (round.secret){
                                countForPlayers.add(nextPlayer);
                            } else {
                                countForPlayers.addAll(players.stream().filter(player -> round.playersWithCaller.contains(player) == round.playersWithCaller.contains(nextPlayer)).collect(Collectors.toSet()));
                            }

                            int sum = 0;
                            for(Player player: countForPlayers){
                                sum += round.scoreBoard.get(player);
                            }

                            return sum >= 66;
                        } else {
                            return false;
                        }
                    case KOPP:
                        return round.turn.ended;
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
            if(RoundState.CALL_CARD.equals(round.roundState)){
                Integer csapIndex = snapszerMoveDTO.getCsapIndex();
                if (csapIndex != null){
                    round.csapCard = round.csapCards.get(csapIndex);
                    round.roundState = RoundState.CALL_FIGURE;
                } else {
                    round.callCard(snapszerMoveDTO.getCard());
                }

            } else if(RoundState.CALL_FIGURE.equals(round.roundState)){
                round.callFigure(snapszerMoveDTO.getCsapFigure());

            } else if (RoundState.LICIT.equals(round.roundState)){
                Licit licit = snapszerMoveDTO.getLicit();
                if (Licit.CHECK.equals(licit)){
                    round.checkLength ++;
                } else {
                    round.checkLength = 0;
                }
                switch (licit){
                    case CHECK:
                        if (round.checkLength == 4 || (round.checkLength == 3 && !round.firstLicitTurn)){ //FIXME snapszer check check check / max limit / passz snapszer passz passz / ?
                            round.roundState = RoundState.TURNS;
                            nextPlayer = round.caller;
                            round.turn = new Turn(nextPlayer);
                            round.turns.add(round.turn);
                        } else {
                            nextPlayer = getNextPlayer();
                            if (round.caller.equals(nextPlayer)){
                                round.firstLicitTurn = false;
                            }
                        }
                        return;

                    case SNAPSZER:
                        round.snapszer = true;
                        nextPlayer = getNextPlayer();
                        if (round.caller.equals(nextPlayer)){
                            round.firstLicitTurn = false;
                        }
                        return;

                    case CONTRA:
                        round.turnValue = round.turnValue.getNextTurnValue();
                        nextPlayer = getNextPlayer();
                        if (round.caller.equals(nextPlayer)){
                            round.firstLicitTurn = false;
                        }
                        return;

                    case CONTRA_SNAPSZER:
                        round.turnValue = round.turnValue.getNextTurnValue();
                        round.snapszer = true;
                        nextPlayer = getNextPlayer();
                        if (round.caller.equals(nextPlayer)){
                            round.firstLicitTurn = false;
                        }
                        return;

                    case THROW_IN:
                        endRound(EndRound.THROW_IN);
                        return;

                    case THREE_NINE:
                        rounds.remove(round);
                        round = new Round(round.caller);
                        rounds.add(round);
                        return;
                }
            } else if (RoundState.TURNS.equals(round.roundState)){
                ActionType actionType = snapszerMoveDTO.getActionType();
                switch (actionType){
                    case TWENTY:
                        round.turn.twenty = true;
                        round.scoreBoard.score(nextPlayer, 20);
                        return;

                    case FORTY:
                        round.turn.forty = true;
                        round.scoreBoard.score(nextPlayer, 40);
                        return;

                    case PLAY_CARD:
                        Card card = snapszerMoveDTO.getCard();
                        nextPlayer.getCards().remove(card);
                        if (card.equals(round.calledCard)){
                            round.secret = false;
                        }
                        round.turn.playCard(nextPlayer, card);
                        if (round.turn.cards.size() != players.size()){
                            nextPlayer = getNextPlayer();
                        } else {
                            endTurn();
                        }
                        return;

                    case STOP:
                        endRound(EndRound.STOP);
                        return;

                    case KOPP:
                        kopp();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    enum EndRound {
        NO_CARDS,
        SNAPSZER_FAIL,
        STOP,
        THROW_IN
    }

    private void endRound(EndRound endRound) {
        final Set<Player> countForPlayers = Sets.newHashSet();
        int point = 1;

        switch (endRound){
            case SNAPSZER_FAIL:{
                countForPlayers.addAll(players.stream().filter(p-> !round.playersWithCaller.contains(p)).collect(Collectors.toSet()));
                point = 6 * round.turnValue.getValue();
                break;
            }
            case NO_CARDS: {
                countForPlayers.addAll(players.stream().filter(p-> round.playersWithCaller.contains(p) == round.playersWithCaller.contains(nextPlayer)).collect(Collectors.toSet()));
                int snapszer = round.snapszer ? 6 : 1;
                int modifier = 1;
                if (!round.snapszer){
                    int pointsForOtherTeam = 0;
                    Set<Player> otherTeam = players.stream().filter(player -> !countForPlayers.contains(player)).collect(Collectors.toSet());
                    for (Player player: otherTeam){
                        pointsForOtherTeam += round.scoreBoard.get(player);
                    }
                    if (otherTeam.stream().allMatch(player -> round.turns.stream().noneMatch(turn -> player.equals(turn.strongestPlayer)))){
                        modifier = 3;
                    } else if(pointsForOtherTeam < 33){
                        modifier = 2;
                    }
                }
                point = snapszer * round.turnValue.getValue() * modifier;
                break;
            }
            case STOP: {
                if (round.secret){
                    countForPlayers.add(nextPlayer);
                } else {
                    countForPlayers.addAll(players.stream().filter(p-> round.playersWithCaller.contains(p) == round.playersWithCaller.contains(nextPlayer)).collect(Collectors.toList()));
                }
                int snapszer = round.snapszer ? 6 : 1;
                int modifier = 1;
                if (!round.snapszer){
                    int pointsForOtherTeam = 0;
                    Set<Player> otherTeam = players.stream().filter(player -> !countForPlayers.contains(player)).collect(Collectors.toSet());
                    for (Player player: otherTeam){
                        pointsForOtherTeam += round.scoreBoard.get(player);
                    }
                    if (otherTeam.stream().allMatch(player -> round.turns.stream().noneMatch(turn -> player.equals(turn.strongestPlayer)))){
                        modifier = 3;
                    } else if(pointsForOtherTeam < 33){
                        modifier = 2;
                    }
                }
                point = snapszer * round.turnValue.getValue() * modifier;
                break;
            }
            case THROW_IN: {
                countForPlayers.addAll(players.stream().filter(player -> !round.playersWithCaller.contains(player)).collect(Collectors.toSet()));
                point = 2;
                break;
            }
        }

        round.finalScoreBoard = new ScoreBoard(players);
        for(Player player: countForPlayers){
            round.finalScoreBoard.score(player, point);
            scoreBoard.score(player, point);
        }

        if (scoreBoard.max() > 32){
            gameState = GameState.ENDED;
        } else {
            nextPlayer = getNextPlayer(round.caller);
            round = new Round(nextPlayer);
            rounds.add(round);
        }
    }

    private void endTurn() {
        int sum = 0;
        for(Card card: round.turn.cards){
            sum += card.getFigure().getValue();
        }
        round.scoreBoard.score(round.turn.strongestPlayer, sum);

        round.turn.ended = true;

        nextPlayer = round.turn.strongestPlayer;
    }

    private void kopp() {
        if(round.snapszer && !round.playersWithCaller.contains(nextPlayer)){
            endRound(EndRound.SNAPSZER_FAIL);
        } else {
            if (nextPlayer.getCards().isEmpty()){
                endRound(EndRound.NO_CARDS);
            } else {
                round.turn = new Turn(nextPlayer);
                round.turns.add(round.turn);
            }
        }
    }

    @Override
    public void surrender(UserDTO userDTO) {

    }

    @Override
    public void restart() {
        nextPlayer = null;
        round = null;
        scoreBoard.clear();
        rounds = new ArrayList<>();
    }

    private Player getPlayer(UserDTO user){
        return players.stream().filter(player -> player.getUser().equals(user)).findFirst().orElse(null);
    }

    private Player getNextPlayer(Player player) {
        return players.get((players.indexOf(player) + 1) % players.size());
    }

    private Player getNextPlayer() {
        return players.get((players.indexOf(nextPlayer) + 1) % players.size());
    }


    public class Round {
        RoundState roundState = RoundState.CALL_CARD;
        Player caller;
        Set<Player> playersWithCaller = new HashSet<>();
        List<Card> csapCards = new ArrayList<>();
        Card calledCard = null;
        Color adu = null;
        TurnValue turnValue = TurnValue.BASIC;
        Card csapCard = null;
        boolean snapszer = false;
        boolean firstLicitTurn = true;
        int checkLength = 0;
        boolean secret = true;
        ScoreBoard scoreBoard;
        ScoreBoard finalScoreBoard = null;
        Turn turn = null;
        List<Turn> turns = new ArrayList<>();

        public Round(Player player){
            caller = player;
            playersWithCaller.add(caller);
            players.forEach(p -> p.getCards().clear());
            dealCards();
            scoreBoard = new ScoreBoard(players);
        }

        private void callCard(Card card) {
            calledCard = card;
            adu = card.getColor();
            caller.getCards().addAll(round.csapCards);
            csapCards.clear();
            playersWithCaller.addAll(players.stream().filter(player -> player.getCards().contains(card)).collect(Collectors.toSet()));
            roundState = RoundState.LICIT;
        }

        private void callFigure(Figure figure) {
            callCard(new Card(round.csapCard.getColor(), figure));
        }

        private void dealCards(){
            Player player_1 = getNextPlayer(caller);
            Player player_2 = getNextPlayer(player_1);
            Player player_3 = getNextPlayer(player_2);

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

        Turn getLastEndedTurn() {
            Turn result = null;
            for(Turn turn: turns){
                if (turn.ended){
                    result = turn;
                }
            }
            return result;
        }
    }


    public class Turn {
        public boolean ended = false;
        List<Card> cards = Lists.newArrayList();
        Card strongestCard = null;
        Card firstCard = null;
        Player caller;
        Player strongestPlayer;
        boolean twenty = false;
        boolean forty = false;

        Turn(Player player){
            caller = player;
        }

        void playCard(Player player, Card card){
            if (cards.isEmpty()){
                firstCard = card;
                strongestCard = card;
                strongestPlayer = player;
            } else {
                if (strongestCard.getColor().equals(round.adu)){
                    if (card.getColor().equals(round.adu) && card.getFigure().strongerThan(strongestCard.getFigure())){
                        strongestCard = card;
                        strongestPlayer = player;
                    }
                } else if (card.getColor().equals(round.adu)){
                    strongestCard = card;
                    strongestPlayer = player;
                } else if (strongestCard.getColor().equals(card.getColor()) && card.getFigure().strongerThan(strongestCard.getFigure())){
                    strongestCard = card;
                    strongestPlayer = player;
                }
            }
            cards.add(card);
        }
    }

}
