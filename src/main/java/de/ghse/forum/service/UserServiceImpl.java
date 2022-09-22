package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import de.ghse.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @Override
    public User save(User user) {
       return userRepository.save(user);
    }

    @Override
    public void addPostToUser(UUID user, UUID post) {
        User user1 = userRepository.findById(user).orElseThrow();
        Post post1 = postRepository.findById(post).orElseThrow();
        user1.getPosts().add(post1);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
