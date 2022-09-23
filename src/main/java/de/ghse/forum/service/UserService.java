package de.ghse.forum.service;

import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository UserRepository;

    public void addUser(User user) { UserRepository.save(user); }
    public  @NotNull Optional<User> findUserByUsername(String username){ return Optional.ofNullable(UserRepository.findByUsername(username)); }
    public  @NotNull Optional<User> findUserById(UUID id){ return UserRepository.findById(id); }
    public  @NotNull Iterable<User> getAllUsers(){ return UserRepository.findAll(); }
}
