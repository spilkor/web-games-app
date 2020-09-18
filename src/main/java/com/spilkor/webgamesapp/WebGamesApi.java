package com.spilkor.webgamesapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.model.User;
import com.spilkor.webgamesapp.util.dto.ChatMessageDTO;
import com.spilkor.webgamesapp.util.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;
import com.spilkor.webgamesapp.util.dto.UserDTO;
import com.spilkor.webgamesapp.util.dto.UserTokenDTO;
import com.spilkor.webgamesapp.util.ConnectionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class WebGamesApi {

    @Autowired
    BusinessManager businessManager;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/userToken")
    public String getUserToken(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest( request);

        UserTokenDTO userToken = new UserTokenDTO();
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setUserId(user.getId());

        ConnectionHandler.addUserToken(userToken);

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessageType(WebSocketMessageType.USER_TOKEN);
        try {
            webSocketMessage.setData(mapper.writeValueAsString(userToken.getToken()));
            return mapper.writeValueAsString(webSocketMessage);
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

        session.setAttribute("user", new UserDTO(foundUser));
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

        session.setAttribute("user", new UserDTO(newUser));
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
                data = mapper.writeValueAsString(chatMessageDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            webSocketMessage.setData(data);
            webSocketMessage.setMessageType(WebSocketMessageType.CHAT_MESSAGE);

            WebSocketMessageSender.broadcastMessage(webSocketMessage);
        }
    }

    @GetMapping(path = "/logout" )
    public void logout( HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        request.getSession(true);

        if (user!=null && user.getId()!=null){
            ConnectionHandler.removeAndCloseConnections(user.getId());
        }
    }

    @GetMapping("/user")
    public String getUser(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        if (user == null){
            return null;
        }

        try {
            return mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
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

}