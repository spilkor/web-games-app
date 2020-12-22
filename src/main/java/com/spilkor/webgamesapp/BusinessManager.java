package com.spilkor.webgamesapp;

import com.spilkor.webgamesapp.model.BaseEntity;
import com.spilkor.webgamesapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BusinessManager {

    @Autowired
    CrudManager crudManager;

    public User findUserByUserName(String userName)  {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " WHERE user.userName = ?1", User.class, userName);
    }

    public User findUserById(Long id) {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " WHERE user.id = ?1", User.class, id);
    }

    public User findEagerUserById(Long id) {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " LEFT JOIN FETCH user.friends " +
                " WHERE user.id = ?1", User.class, id);
    }

    public <E extends BaseEntity> E[] save(E ...entities) {
        for(E entity: entities){
            save(entity);
        }
        return entities;
    }

    public void createFriendship(User user1, User user2){
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
        save(user1, user2);
    }

    public void dropFriendship(User user1, User user2){
        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);
        save(user1, user2);
    }


    public <E extends BaseEntity> E save(E entity) {
        return crudManager.save(entity);
    }

    public <E extends BaseEntity> Collection<E> save(Collection<E> entities) {
        entities.forEach(e-> save(e));
        return entities;
    }

    public boolean friends(Long userId_1, Long userId_2) {
        User user_1 = findEagerUserById(userId_1);
        return user_1 != null && user_1.getFriends().stream().anyMatch(f-> f.getId().equals(userId_2));
    }
}
