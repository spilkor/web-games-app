package com.spilkor.webgamesapp.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WG_USER")
public class User extends BaseEntity {

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WG_FRIEND",
            joinColumns={ @JoinColumn(name = "FRIEND_ID") },
            inverseJoinColumns = { @JoinColumn(name = "FRIEND_TO_ID")})
    private Set<User> friends = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "friends")
    private Set<User> friendTo = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WG_FRIEND_REQUEST",
            joinColumns={ @JoinColumn(name = "REQUESTER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "REQUESTED_ID")})
    private Set<User> friendRequests = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "friendRequests")
    private Set<User> requestedBy = new HashSet<>();


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


    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getFriendTo() {
        return friendTo;
    }

    public void setFriendTo(Set<User> friendTo) {
        this.friendTo = friendTo;
    }

    public Set<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Set<User> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Set<User> getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Set<User> requestedBy) {
        this.requestedBy = requestedBy;
    }

}
