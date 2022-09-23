package de.ghse.forum.api;

import de.ghse.forum.model.User;
import de.ghse.forum.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addUser(@Valid @NonNull @RequestBody User user){
        userService.addUser(user);
    }

    @GetMapping(path = "{username}")
    public String getUUID(@PathVariable("username") String username){
        return userService.findUserByUsername(username).get().getId().toString();
    }

    @GetMapping
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
