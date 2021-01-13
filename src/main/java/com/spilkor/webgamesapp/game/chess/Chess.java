package com.spilkor.webgamesapp.game.chess;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.dto.Point;
import com.spilkor.webgamesapp.model.dto.UserDTO;
import com.spilkor.webgamesapp.util.Mapper;
import com.spilkor.webgamesapp.util.MathUtil;


public class Chess extends Game {

    private OwnerAs ownerAs;
    private ChessPiece[][] table;
    private UserDTO nextPlayer;
    private UserDTO startingPlayer;

    public Chess(UserDTO owner, GameType gameType){
        super(owner, gameType);

        ownerAs = OwnerAs.Random;
    }

    @Override
    public boolean updateLobby(String lobbyJSON) {
        try {
            ChessLobbyDTO chessLobbyDTO = Mapper.readValue(lobbyJSON, ChessLobbyDTO.class);

            if (chessLobbyDTO.getOwnerAs() == null){
                return false;
            }

            ownerAs = chessLobbyDTO.getOwnerAs();

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

        switch (ownerAs){
            case WHITE:
                startingPlayer = owner;
                break;
            case BLACK:
                startingPlayer = getSecondPlayer();
                break;
            case Random:
                startingPlayer = MathUtil.coinToss() ? owner : getSecondPlayer();
                break;
        }
        nextPlayer = startingPlayer;
    }

    private ChessPiece[][] getNewChessTable() {
        ChessPiece[][] tempTable = new ChessPiece[8][8];

        tempTable[0][0] = new ChessPiece(ChessPieceType.ROOK, Color.BLACK);
        tempTable[0][1] = new ChessPiece(ChessPieceType.KNIGHT, Color.BLACK);
        tempTable[0][2] = new ChessPiece(ChessPieceType.BISHOP, Color.BLACK);
        tempTable[0][3] = new ChessPiece(ChessPieceType.QUEEN, Color.BLACK);
        tempTable[0][4] = new ChessPiece(ChessPieceType.KING, Color.BLACK);
        tempTable[0][5] = new ChessPiece(ChessPieceType.BISHOP, Color.BLACK);
        tempTable[0][6] = new ChessPiece(ChessPieceType.KNIGHT, Color.BLACK);
        tempTable[0][7] = new ChessPiece(ChessPieceType.ROOK, Color.BLACK);

        tempTable[1][0] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][1] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][2] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][3] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][4] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][5] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][6] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);
        tempTable[1][7] = new ChessPiece(ChessPieceType.PAWN, Color.BLACK);

        tempTable[6][0] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][1] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][2] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][3] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][4] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][5] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][6] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);
        tempTable[6][7] = new ChessPiece(ChessPieceType.PAWN, Color.WHITE);

        tempTable[7][0] = new ChessPiece(ChessPieceType.ROOK, Color.WHITE);
        tempTable[7][1] = new ChessPiece(ChessPieceType.KNIGHT, Color.WHITE);
        tempTable[7][2] = new ChessPiece(ChessPieceType.BISHOP, Color.WHITE);
        tempTable[7][3] = new ChessPiece(ChessPieceType.QUEEN, Color.WHITE);
        tempTable[7][4] = new ChessPiece(ChessPieceType.KING, Color.WHITE);
        tempTable[7][5] = new ChessPiece(ChessPieceType.BISHOP, Color.WHITE);
        tempTable[7][6] = new ChessPiece(ChessPieceType.KNIGHT, Color.WHITE);
        tempTable[7][7] = new ChessPiece(ChessPieceType.ROOK, Color.WHITE);

        return tempTable;
    }

    @Override
    public void restart() {
        table = null;
    }

    @Override
    public String getGameJSON(UserDTO user) {
        ChessGameDTO chessGameDTO = new ChessGameDTO();

        chessGameDTO.setOwnerAs(ownerAs);
        chessGameDTO.setTable(table);
        chessGameDTO.setNextPlayer(nextPlayer);
        chessGameDTO.setStartingPlayer(startingPlayer);

        try {
            return Mapper.writeValueAsString(chessGameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean legal(UserDTO userDTO, String moveJSON) {
        try {
            ChessMoveDTO chessMoveDTO = Mapper.readValue(moveJSON, ChessMoveDTO.class);

            //TODO, FIXME...

            return getChessPiece(chessMoveDTO.getFromPosition()) != null;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    private ChessPiece getChessPiece(Point position){
        return table[position.getX()][position.getY()];
    }

    @Override
    public void move(UserDTO userDTO, String moveJSON) {
        try {
            ChessMoveDTO chessMoveDTO = Mapper.readValue(moveJSON, ChessMoveDTO.class);

//            TODO, FIXME...
            table[chessMoveDTO.getToPosition().getX()][chessMoveDTO.getToPosition().getY()] = getChessPiece(chessMoveDTO.getFromPosition());
            table[chessMoveDTO.getFromPosition().getX()][chessMoveDTO.getFromPosition().getY()] = null;

            nextPlayer = nextPlayer.equals(owner) ? getSecondPlayer() : owner;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
