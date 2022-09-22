package de.ghse.forum.api;

import de.ghse.forum.model.User;
import de.ghse.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController @RequiredArgsConstructor @RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/all")
    public String getAllUsers(){
        return userService.findAll().toString();
    }

    @GetMapping(path = "/{username}")
    public UUID getUserByUsername(@PathVariable("username") String username){
        return userService.findByUsername(username).getId();
    }

    @GetMapping(path = "/{username}/posts")
    public String getUserPosts(@PathVariable("username") String username){
        return userService.findByUsername(username).getPosts().toString();
    }

    @PostMapping(path = "/test/adduser/{username}")
    public String addUser(@PathVariable("username") String username){
        userService.save(new User(UUID.randomUUID(), username, null));
        return "User added";
    }

}
