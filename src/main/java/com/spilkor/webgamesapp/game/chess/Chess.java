package com.spilkor.webgamesapp.game.chess;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.game.chess.dto.ChessGameDTO;
import com.spilkor.webgamesapp.game.chess.dto.ChessLobbyDTO;
import com.spilkor.webgamesapp.game.chess.dto.ChessMoveDTO;
import com.spilkor.webgamesapp.game.chess.enums.*;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.spilkor.webgamesapp.game.chess.enums.Color.BLACK;
import static com.spilkor.webgamesapp.game.chess.enums.Color.WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.DrawOffer.BLACK_TO_WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.DrawOffer.WHITE_TO_BLACK;
import static com.spilkor.webgamesapp.game.chess.enums.DrawReason.*;
import static com.spilkor.webgamesapp.game.chess.enums.OwnerAs.Random;
import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Chess extends Game {

    private OwnerAs ownerAs;
    private List<Player> players = new ArrayList<>();
    private List<ChessPosition> chessPositionList;
    private ChessPosition currentChessPosition;
    private ChessPosition waitingForPromotionChessPosition;
    private Player winner;
    private Player surrendered;
    private boolean draw;
    private DrawReason drawReason;
    private boolean threeFoldRepetition = false;
    private DrawOffer activeDrawOffer = null;

    public Chess(UserDTO owner, GameType gameType) {
        super(owner, gameType);
        ownerAs = Random;
        players.add(new Player(owner));
    }

    @Override
    public boolean isStartable() {
        return players.size() == 2;
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        try {
            ChessLobbyDTO chessLobbyDTO = Mapper.readValue(lobbyJSON, ChessLobbyDTO.class);
            OwnerAs ownerAs = chessLobbyDTO.getOwnerAs();

            if (ownerAs == null) {
                return false;
            }

            this.ownerAs = ownerAs;
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public void start() {
        switch (ownerAs) {
            case WHITE:
                players.forEach(player -> player.setColor(player.getUser().equals(owner) ? WHITE : BLACK));
                break;
            case BLACK:
                players.forEach(player -> player.setColor(player.getUser().equals(owner) ? BLACK : WHITE));
                break;
            case Random:
                if (MathUtil.coinToss()) {
                    players.forEach(player -> player.setColor(player.getUser().equals(owner) ? WHITE : BLACK));
                } else {
                    players.forEach(player -> player.setColor(player.getUser().equals(owner) ? BLACK : WHITE));
                }
                break;
        }

        currentChessPosition = new ChessPosition();

        chessPositionList = new ArrayList<>();
        chessPositionList.add(currentChessPosition);
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
    public boolean legal(UserDTO userDTO, String moveJSON) {
        try {
            ChessMoveDTO chessMoveDTO = Mapper.readValue(moveJSON, ChessMoveDTO.class);
            ChessCoordinate source = chessMoveDTO.getSource();
            ChessCoordinate target = chessMoveDTO.getTarget();
            PieceType promotionType = chessMoveDTO.getPromoteType();
            boolean draw = chessMoveDTO.isDraw();

            if (draw){
                return threeFoldRepetition
                        || (WHITE.equals(getPlayer(userDTO).getColor()) && !WHITE_TO_BLACK.equals(activeDrawOffer))
                        || (BLACK.equals(getPlayer(userDTO).getColor()) && !BLACK_TO_WHITE.equals(activeDrawOffer));
            }

            if (!currentChessPosition.getNextColor().equals(getPlayer(userDTO).getColor())){
                return false;
            }

            if (waitingForPromotionChessPosition != null){
                return Arrays.asList(QUEEN, ROOK, KNIGHT, BISHOP).contains(promotionType);
            } else {
                return source != null && target != null && currentChessPosition.legal(source, target);
            }
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            ChessMoveDTO chessMoveDTO = Mapper.readValue(moveJSON, ChessMoveDTO.class);
            ChessCoordinate source = chessMoveDTO.getSource();
            ChessCoordinate target = chessMoveDTO.getTarget();
            PieceType promotionType = chessMoveDTO.getPromoteType();
            boolean drawMove = chessMoveDTO.isDraw();
            Color color = getPlayer(userDTO).getColor();

            if (drawMove){
                if (threeFoldRepetition){
                    draw = true;
                    gameState = GameState.ENDED;
                    drawReason = THREEFOLD_REPETITION;
                    return;
                } else {
                    if (WHITE.equals(color)){
                        if (BLACK_TO_WHITE.equals(activeDrawOffer)){
                            draw = true;
                            gameState = GameState.ENDED;
                            drawReason = AGREED_TO_DRAW;
                            return;
                        } else {
                            activeDrawOffer = WHITE_TO_BLACK;
                            return;
                        }
                    } else {
                        if (WHITE_TO_BLACK.equals(activeDrawOffer)){
                            draw = true;
                            gameState = GameState.ENDED;
                            drawReason = AGREED_TO_DRAW;
                            return;
                        } else {
                            activeDrawOffer = BLACK_TO_WHITE;
                            return;
                        }
                    }
                }
            } else {
                threeFoldRepetition = false;
                if (WHITE.equals(color)){
                    if (BLACK_TO_WHITE.equals(activeDrawOffer)){
                        activeDrawOffer = null;
                    }
                } else {
                    if (WHITE_TO_BLACK.equals(activeDrawOffer)){
                        activeDrawOffer = null;
                    }
                }
            }

            ChessPosition nextChessPosition;
            if (waitingForPromotionChessPosition != null){
                nextChessPosition = waitingForPromotionChessPosition.promote(promotionType);
                waitingForPromotionChessPosition = null;
            } else {
                nextChessPosition = currentChessPosition.move(source, target);
                if (nextChessPosition.isWaitingForPromotionType()){
                    waitingForPromotionChessPosition = nextChessPosition;
                    return;
                }
            }

            chessPositionList.add(nextChessPosition);
            currentChessPosition = nextChessPosition;

            if (!currentChessPosition.hasLegalMove()){
                gameState = GameState.ENDED;
                if (currentChessPosition.isKingInCheck(currentChessPosition.getNextColor())){
                    winner = players.stream().filter(player -> !currentChessPosition.getNextColor().equals(player.getColor())).findAny().orElse(null);
                } else {
                    draw = true;
                    drawReason = STALEMATE;
                }
            } else if(currentChessPosition.getNumberOfMovesSinceLastPawnMoveOrTake() >= 50){
                gameState = GameState.ENDED;
                draw = true;
                drawReason = FIFTY_MOVE_RULE;
            } else if(!currentChessPosition.hasSufficientMaterial()){
                gameState = GameState.ENDED;
                draw = true;
                drawReason = INSUFFICIENT_MATERIAL;
            } else {
                int repetitionCount = (int) chessPositionList.stream().filter(chessPosition -> chessPosition.equals(currentChessPosition)).count();
                if (repetitionCount > 4){
                    gameState = GameState.ENDED;
                    draw = true;
                    drawReason = FIVEFOLD_REPETITION;
                } else if(repetitionCount > 2){
                    threeFoldRepetition = true;
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surrender(UserDTO userDTO) {
        gameState = GameState.ENDED;
        surrendered = players.stream().filter(player -> player.getUser().equals(userDTO)).findAny().orElse(null);
    }

    @Override
    public String getGameJSON(UserDTO user) {
        ChessGameDTO chessGameDTO = new ChessGameDTO();

        chessGameDTO.setPlayers(players);
        if (currentChessPosition != null){
            chessGameDTO.setTable(currentChessPosition.convertTable());
            chessGameDTO.setNextColor(currentChessPosition.getNextColor());
        }
        chessGameDTO.setWinner(winner);
        chessGameDTO.setDraw(draw);
        chessGameDTO.setSurrendered(surrendered);
        chessGameDTO.setOwnerAs(ownerAs);
        chessGameDTO.setDrawReason(drawReason);
        chessGameDTO.setWaitingForPromotionType(waitingForPromotionChessPosition != null);

        Color color = getPlayer(user).getColor();
        if (WHITE.equals(color)){
            chessGameDTO.setDrawActive(threeFoldRepetition || BLACK_TO_WHITE.equals(activeDrawOffer));
        } else {
            chessGameDTO.setDrawActive(threeFoldRepetition || WHITE_TO_BLACK.equals(activeDrawOffer));
        }

        try {
            return Mapper.writeValueAsString(chessGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void restart() {
        chessPositionList = null;
        currentChessPosition = null;
        draw = false;
        winner = null;
        surrendered = null;
        players.forEach(player -> player.setColor(null));
        waitingForPromotionChessPosition = null;
        threeFoldRepetition = false;
        activeDrawOffer = null;
    }

}
