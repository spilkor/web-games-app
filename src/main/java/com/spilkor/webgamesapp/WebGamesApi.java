package com.spilkor.webgamesapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spilkor.webgamesapp.game.Game;
import com.spilkor.webgamesapp.model.User;
import com.spilkor.webgamesapp.model.dto.*;
import com.spilkor.webgamesapp.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class WebGamesApi {

    final
    BusinessManager businessManager;

    @Autowired
    public WebGamesApi(BusinessManager businessManager) {
        this.businessManager = businessManager;
    }

    @GetMapping("/userToken")
    public String getUserToken(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        UserTokenDTO userToken = new UserTokenDTO();
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setUserId(user.getId());

        ConnectionHandler.addUserToken(userToken);

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessage.MessageType.USER_TOKEN);
        try {
            webSocketMessage.setData(Mapper.writeValueAsString(userToken.getToken()));
            return Mapper.writeValueAsString(webSocketMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void login(@RequestBody UserNameAndPassword userNameAndPassword, HttpServletResponse response, HttpServletRequest request) {
        UserDTO oldUser = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        if (oldUser!=null){
            ConnectionHandler.removeAndCloseConnections(oldUser.getId());
        }

        if (userNameAndPassword.getUserName()==null || userNameAndPassword.getPassword()==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User foundUser = businessManager.findUserByUserName(userNameAndPassword.getUserName());

        if (foundUser == null || !userNameAndPassword.getPassword().equals(foundUser.getPassword())){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        session.setAttribute("user", new UserDTO(foundUser, UserDTO.UserState.online));
    }


    @GetMapping(path = "/invite/{friendId}")
    public void invite(@PathVariable Long friendId, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        User friend = businessManager.findUserById(friendId);
        if (friend == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        UserDTO friendDTO = new UserDTO(friend);

        try {
            GameHandler.invite(user, friendDTO);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }

    @GetMapping(path = "/accept-invite/{ownerId}")
    public void acceptInvite(@PathVariable Long ownerId, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        User owner = businessManager.findUserById(ownerId);
        if (owner == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        UserDTO ownerDTO = new UserDTO(owner);

        try {
            GameHandler.acceptInvite(user, ownerDTO);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }

    @GetMapping(path = "/kick-player/{playerId}")
    public void kickPlayer(@PathVariable Long playerId, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        User player = businessManager.findUserById(playerId);
        if (player == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        UserDTO playerDTO = new UserDTO(player);

        try {
            GameHandler.kickPlayer(user, playerDTO);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }


    @GetMapping(path = "/decline-invite/{ownerId}")
    public void declineInvite(@PathVariable Long ownerId, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        User owner = businessManager.findUserById(ownerId);
        if (owner == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        UserDTO ownerDTO = new UserDTO(owner);

        try {
            GameHandler.declineInvite(user, ownerDTO);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }

    @GetMapping(path = "/start-game")
    public void startGame(HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
        try {
            GameHandler.startGame(user);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }

    @GetMapping(path = "/restart-game")
    public void restartGame(HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
        try {
            GameHandler.restartGame(user);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }

    @PostMapping(path = "/create-account", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void createAccount(@RequestBody UserNameAndPassword userNameAndPassword, HttpServletResponse response, HttpServletRequest request) {
        UserDTO oldUser = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        if (oldUser!=null){
            ConnectionHandler.removeAndCloseConnections(oldUser.getId());
        }

        if (userNameAndPassword.getUserName()==null || userNameAndPassword.getPassword()==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User foundUser = businessManager.findUserByUserName(userNameAndPassword.getUserName());

        if (foundUser != null){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        if (userNameAndPassword.getUserName().length() < 3 || userNameAndPassword.getPassword().length() < 3
            || userNameAndPassword.getUserName().length() > 9 || userNameAndPassword.getPassword().length() > 9
        ){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        User newUser = new User();
        newUser.setUserName(userNameAndPassword.getUserName());
        newUser.setPassword(userNameAndPassword.getPassword());
        businessManager.save(newUser);

        session.setAttribute("user", new UserDTO(newUser, UserDTO.UserState.online));
    }

    @PostMapping(path = "/remove-friend", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void removeFriend(@RequestBody Long id, HttpServletResponse response, HttpServletRequest request) {
        User user = businessManager.findUserById(getUserDTOFromRequest(request).getId());
        User friend = businessManager.findUserById(id);

        if(id == null || id.equals(user.getId()) || friend == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!user.getFriends().contains(friend)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        businessManager.dropFriendship(user, friend);

        List<User> toUpdate = new ArrayList<>();
        toUpdate.add(user);
        toUpdate.add(friend);
        ConnectionHandler.updateFriendList(toUpdate);
    }

    @PostMapping(path = "/move", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void move(@RequestBody String moveJSON, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
        try {
            GameHandler.move(user, moveJSON);
        } catch (WebGamesApiException webGamesApiException){
            response.setStatus(webGamesApiException.getStatus());
        }
    }

    @PostMapping(path = "/friend-request", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void friendRequest(@RequestBody String userName, HttpServletResponse response, HttpServletRequest request) {
        UserDTO userDTO = getUserDTOFromRequest(request);

        if(userName == null || userName.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (userName.equals(userDTO.getName())){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        User foundUser = businessManager.findUserByUserName(userName);
        if (foundUser == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        User currentUser = businessManager.findUserByUserName(userDTO.getName());
        if (currentUser.getFriends().contains(foundUser)){
            response.setStatus(HttpServletResponse.SC_FOUND);
            return;
        }

        if (currentUser.getFriendRequests().contains(foundUser)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //OK...

        if (foundUser.getFriendRequests().contains(currentUser)){
            foundUser.getFriendRequests().remove(currentUser);
            businessManager.createFriendship(currentUser, foundUser);

            List<User> toUpdate = new ArrayList<>();
            toUpdate.add(currentUser);
            toUpdate.add(foundUser);
            ConnectionHandler.updateFriendList(toUpdate);
            return;
        }

        currentUser.getFriendRequests().add(foundUser);
        businessManager.save(currentUser);
    }

    @PostMapping(path = "/chat-message", consumes = MediaType.APPLICATION_JSON_VALUE  )
    public void chatMessage(@RequestBody String text, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        if(!StringUtils.isEmpty(text)){
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setMessage(text);
            chatMessageDTO.setUser(user);

            WebSocketMessage webSocketMessage = new WebSocketMessage();
            String data = null;
            try {
                data = Mapper.writeValueAsString(chatMessageDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            webSocketMessage.setData(data);
            webSocketMessage.setMessageType(WebSocketMessage.MessageType.CHAT_MESSAGE);

            WebSocketMessageSender.broadcastMessage(webSocketMessage);
        }
    }

    @GetMapping(path = "/logout" )
    public void logout( HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        request.getSession(true);

        ConnectionHandler.removeAndCloseConnections(user.getId());
    }

    @GetMapping("/user")
    public String getUser(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        if (user == null){
            return null;
        }

        try {
            return Mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/game")
    public String getGameData(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            return null;
        }

        GameDTO gameDTO = game.getGameDTO(user);

        try {
            return Mapper.writeValueAsString(gameDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/my-invites")
    public String getMyInvites(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        Set<UserDTO> invites = GameHandler.getInvites(user);

        try {
            return Mapper.writeValueAsString(invites);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/my-friends")
    public String getMyFriends(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        List<UserDTO> friendDTOs = businessManager.findUserById(user.getId()).getFriends().stream().map(UserDTO::new).collect(Collectors.toList());
        friendDTOs.forEach(friendDTO-> friendDTO.setUserState(ConnectionHandler.getUserState(friendDTO.getId())));

        try {
            return Mapper.writeValueAsString(friendDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/leave-game")
    public void leaveGame(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
        try {
            GameHandler.leaveGame(user);
        } catch (WebGamesApiException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(path = "/create-game/{gameType}")
    public void createGame(@PathVariable Game.GameType gameType, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        Game game = GameHandler.getGameOfUser(user);

        if (game != null){
            response.setStatus(HttpServletResponse.SC_FOUND);
            return;
        }

        game = GameHandler.createGame(user, gameType);
        GameHandler.updatePlayers(game);
    }

    @PostMapping(path = "/lobby", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void updateLobby(@RequestBody String lobbyJSON, HttpServletResponse response, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!game.getOwner().equals(user)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        boolean success = game.updateLobby(lobbyJSON);

        if (!success){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GameHandler.updatePlayers(game);
    }

    private UserDTO getUserDTOFromRequest(HttpServletRequest request) {
        return (UserDTO) request.getSession().getAttribute("user");
    }


    private static class UserNameAndPassword implements Serializable {
        String userName;
        String password;

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }


    public static class WebGamesApiException extends Exception{

        public WebGamesApiException(int status) {
            this.status = status;
        }

        private int status;


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }


}