package com.spilkor.webgamesapp.game.chess;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.game.chess.dto.ChessGameDTO;
import com.spilkor.webgamesapp.game.chess.dto.ChessLobbyDTO;
import com.spilkor.webgamesapp.game.chess.dto.ChessMoveDTO;
import com.spilkor.webgamesapp.game.chess.enums.Color;
import com.spilkor.webgamesapp.game.chess.enums.PieceType;
import com.spilkor.webgamesapp.game.chess.pieces.Piece;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;

import java.util.Arrays;

import static com.spilkor.webgamesapp.game.chess.enums.Color.BLACK;
import static com.spilkor.webgamesapp.game.chess.enums.Color.WHITE;
import static com.spilkor.webgamesapp.game.chess.enums.PieceType.*;

public class Chess extends Game {

    private static final PieceType[] PIECE_ORDER = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};

    private Piece[][] table;

    private Position whiteKing = new Position(7, 4);
    private Position blackKing = new Position(0, 4);

    private Color ownerColor;
    private UserDTO nextPlayer;

    private boolean draw;
    private UserDTO winner;


    public Chess(UserDTO owner, GameType gameType) {
        super(owner, gameType);
    }

    @Override
    public void surrender(UserDTO userDTO) {
        gameState = GameState.ENDED;
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        try {
            ChessLobbyDTO chessLobbyDTO = Mapper.readValue(lobbyJSON, ChessLobbyDTO.class);

            ownerColor = chessLobbyDTO.getOwnerColor();
            nextPlayer = WHITE.equals(ownerColor) ? owner : getSecondPlayer();

            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStartable() {
        return players.size() == 2;
    }

    @Override
    public void start() {
        table = getNewChessTable();
    }

    private Piece[][] getNewChessTable() {
        Piece[][] table = new Piece[8][8];

        for (int i = 0; i < table.length; i++) {
            for (int k = 0; k < table[i].length; k++) {
                switch (i) {
                    case 0:
                        table[i][k] = PieceFactory.create(PIECE_ORDER[k], BLACK, table);
                        break;
                    case 1:
                        table[i][k] = PieceFactory.create(PAWN, BLACK, table);
                        break;
                    case 6:
                        table[i][k] = PieceFactory.create(PAWN, WHITE, table);
                        break;
                    case 7:
                        table[i][k] = PieceFactory.create(PIECE_ORDER[k], WHITE, table);
                        break;
                    default:
                        table[i][k] = null;
                }
            }
        }

        return table;
    }

    @Override
    public void restart() {
        table = null;
        nextPlayer = null;
        ownerColor = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        ChessGameDTO chessGameDTO = new ChessGameDTO();

        chessGameDTO.setOwnerColor(ownerColor);
        chessGameDTO.setTable(table);
        chessGameDTO.setNextPlayer(nextPlayer);
        chessGameDTO.setDraw(draw);
        chessGameDTO.setWinner(winner);

        try {
            return Mapper.writeValueAsString(chessGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {

        boolean legal = false;

        try {
            ChessMoveDTO chessMoveDTO = Mapper.readValue(moveJSON, ChessMoveDTO.class);
            Piece actualPiece = table[chessMoveDTO.getSource().getRow()][chessMoveDTO.getSource().getColumn()];

            Position source = chessMoveDTO.getSource();
            Position target = chessMoveDTO.getTarget();

            if (actualPiece == null || nextPlayer.equals(userDTO) || chessMoveDTO.getTarget().equals(chessMoveDTO.getSource())) {
                return false;
            }

            legal = actualPiece.validateMove(source, target);

            // check if capture is on same color
            if ((getPiece(chessMoveDTO.getTarget()) != null) && legal) {
                legal = !getPiece(chessMoveDTO.getTarget()).getColor().equals(getPiece(chessMoveDTO.getSource()).getColor());
            }

            if (legal) {
                legal = isPlayerInCheck(source, target);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return legal;
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            ChessMoveDTO chessMoveDTO = Mapper.readValue(moveJSON, ChessMoveDTO.class);
            Piece targetPiece = getPiece(chessMoveDTO.getSource());

            // promote
            if (chessMoveDTO.getPromoteType() != null && PAWN.equals(targetPiece.getType())) {
                boolean validRow = getUserColor(userDTO).equals(WHITE) ? chessMoveDTO.getTarget().getRow() == 0 : chessMoveDTO.getTarget().getRow() == 7;
                if (validRow) {
                    targetPiece = PieceFactory.create(chessMoveDTO.getPromoteType(), getUserColor(userDTO), table);
                }
            }

            table[chessMoveDTO.getTarget().getRow()][chessMoveDTO.getTarget().getColumn()] = targetPiece;
            table[chessMoveDTO.getSource().getRow()][chessMoveDTO.getSource().getColumn()] = null;

            // update king positions
            if (targetPiece != null && KING.equals(targetPiece.getType())) {
                if (WHITE.equals(targetPiece.getColor()))
                    whiteKing = chessMoveDTO.getTarget();
                else {
                    blackKing = chessMoveDTO.getTarget();
                }
            }

            checkWinCondition();

            nextPlayer = owner.equals(userDTO) ? getSecondPlayer() : owner;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void checkWinCondition() {
        // TODO
    }

    private boolean isPlayerInCheck(Position source, Position target) {
        Piece[][] tableCopy = Arrays.stream(table).map(Piece[]::clone).toArray($ -> table.clone());

        Piece actualPiece = tableCopy[source.getRow()][source.getColumn()];

        tableCopy[target.getRow()][target.getColumn()] = getPiece(source);
        tableCopy[source.getRow()][source.getColumn()] = null;

        for (int i = 0; i < tableCopy.length; i++) {
            for (int k = 0; k < tableCopy[i].length; k++) {
                Piece piece = tableCopy[i][k];
                if (piece != null && !piece.getColor().equals(actualPiece.getColor())) {
                    Position opponentPosition = WHITE.equals(actualPiece.getColor()) ? whiteKing : blackKing;
                    if (!getPiece(opponentPosition).equals(piece) && piece.validateMove(new Position(i, k), opponentPosition)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private Piece getPiece(Position position) {
        return table[position.getRow()][position.getColumn()];
    }

    private Color getUserColor(UserDTO user) {
        if (user.equals(owner)) {
            return ownerColor;
        } else {
            return WHITE.equals(ownerColor) ? BLACK : WHITE;
        }
    }
}
