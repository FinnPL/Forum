package de.ghse.forum.service;

import de.ghse.forum.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User save(User user);
    void addPostToUser(UUID user, UUID post);
    User findByUsername(String username);
    List<User> findAll();
}
