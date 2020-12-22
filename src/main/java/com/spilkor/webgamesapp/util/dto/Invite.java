package com.spilkor.webgamesapp.util.dto;


public class Invite {

    public enum InviteState {
        PENDING
    }

    private UserDTO owner;
    private UserDTO friend;
    private InviteState inviteState = InviteState.PENDING;

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public UserDTO getFriend() {
        return friend;
    }

    public void setFriend(UserDTO friend) {
        this.friend = friend;
    }

    public InviteState getInviteState() {
        return inviteState;
    }

    public void setInviteState(InviteState inviteState) {
        this.inviteState = inviteState;
    }
}
