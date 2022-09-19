package de.ghse.forum.api;

import de.ghse.forum.model.Post;
import de.ghse.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("api/v1/post")
@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public void addPost(@Valid @NonNull @RequestBody Post post){
        postService.addPost(post);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping(path = "{id}")
    public Post getPostById(@PathVariable("id") UUID id){
        //Return Post by ID or throw exception (404)
        return postService.getPostById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find resource"));
    }

    @DeleteMapping(path = "{id}")
    public void deletePost(@PathVariable("id") UUID id){
        postService.deletePost(id);
    }

    @PutMapping(path = "{id}")
    public void updatePost(@PathVariable("id") UUID id,@Valid @NonNull @RequestBody Post post){
        postService.updatePost(id, post);
    }

}
