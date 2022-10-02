package de.ghse.forum.api;

import de.ghse.forum.model.User;
import de.ghse.forum.service.UserService;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //debug Requests: **************************************************************************************************************************************************
    @PostMapping
    public void addUser(@Valid @NonNull @RequestBody UserRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setId(UUID.randomUUID());
        userService.addUser(user);
    }

    @GetMapping
    public Iterable<UserResponse> getAllUsers(){
        return new UserResponse().convert(userService.getAllUsers());
    }


    //API Requests: ****************************************************************************************************************************************************
    @GetMapping(path = "{id}")
    public UserResponse getUser(@PathVariable("id") UUID id){
        return  new UserResponse().convert(userService.findUserById(id).orElseThrow());
    }


    //Response and Request Classes: *************************************************************************************************************************************
    @Data
    public static class UserResponse {
        private String username;
        private UUID id;

        public Iterable<UserResponse> convert(Iterable<User> allUsers) {
            List<UserResponse> userResponses = new ArrayList<>();
            for (User user : allUsers) {
                UserResponse userResponse = new UserResponse();
                userResponse.setUsername(user.getUsername());
                userResponse.setId(user.getId());
                userResponses.add(userResponse);
            }
            return userResponses;
        }
        public UserResponse convert(User user){
            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(user.getUsername());
            userResponse.setId(user.getId());
            return userResponse;
        }
    }
    @Data
    public static class UserRequest {
        private String username;
    }
}
