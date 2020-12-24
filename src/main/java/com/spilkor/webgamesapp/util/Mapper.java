package com.spilkor.webgamesapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spilkor.webgamesapp.BusinessManager;
import com.spilkor.webgamesapp.WebSocketMessageSender;
import com.spilkor.webgamesapp.model.User;
import com.spilkor.webgamesapp.util.dto.AmobaLobbyDTO;
import com.spilkor.webgamesapp.util.dto.UserDTO;
import com.spilkor.webgamesapp.util.dto.UserTokenDTO;
import com.spilkor.webgamesapp.util.dto.WebSocketMessage;
import com.spilkor.webgamesapp.util.enums.UserState;
import com.spilkor.webgamesapp.util.enums.WebSocketMessageType;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public abstract class Mapper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <CLASS> CLASS readValue(String value, Class<CLASS> clazz) throws JsonProcessingException {
        return mapper.readValue(value, clazz);
    }

    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

}
