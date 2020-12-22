package com.spilkor.webgamesapp.util.dto;

import com.spilkor.webgamesapp.model.User;
import com.spilkor.webgamesapp.util.enums.UserState;

import java.util.Objects;

public class UserDTO {

    Long id;
    String name;
    UserState userState;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getUserName();
    }

    public UserDTO(User user, UserState userState) {
        this.id = user.getId();
        this.name = user.getUserName();
        this.userState = userState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
