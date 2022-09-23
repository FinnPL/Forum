package de.ghse.forum.api;

import de.ghse.forum.model.Post;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("api/v1")
@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping(path = "/post")
    public void addPost( @RequestBody  PostRequest postRequest){
        Post post = new Post(UUID.randomUUID(), postRequest.getTitle(), postRequest.getContent(), userService.findUserById(UUID.fromString(postRequest.getUser_id())).orElseThrow() , new Date());
        postService.addPost(post);
    }

    @Data
    public static class PostRequest {
        private String title;
        private String content;
        private String user_id;
    }

    @GetMapping(path = "/post")
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping(path = "/post/{id}")
    public Post getPostById(@PathVariable("id") UUID id){
        return postService.getPostById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find resource"));
    }

    @DeleteMapping(path = "/post/{id}")
    public void deletePost(@PathVariable("id") UUID id){
        postService.deletePost(id);
    }

    @PutMapping(path = "/post/{id}")
    public void updatePost(@PathVariable("id") UUID id,@Valid @NonNull @RequestBody Post post){
        postService.updatePost(id, post);
    }
    @GetMapping(path = "title/{title}")
    public List<Post> getAllByTitleContaining(@PathVariable("title") String title){ return postService.getAllByTitleContaining(title); }
}
