package de.ghse.forum.api;

import de.ghse.forum.model.Post;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("api/v1/post")
@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    //debug Requests: **************************************************************************************************************************************************

    @GetMapping(path = "/all")
    public List<PostResponse> getAllPosts(){
        return new PostResponse().convert(postService.getAllPosts());
    }


    //API Requests: ****************************************************************************************************************************************************

    @PostMapping(path = "/add")
    public ResponseEntity<PostResponse> addPost(@RequestBody  PostRequest postRequest){
        Post post = new Post(UUID.randomUUID(), postRequest.getTitle(), postRequest.getContent(),
                userService.findUserById(UUID.fromString(postRequest.getUser_id())).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Can not add Post: User not found")),
                new Date().toString());
        postService.addPost(post);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/post/add").toUriString());
        return ResponseEntity.created(uri).body(new PostResponse().convert(post));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("id") UUID id){
        return ResponseEntity.ok().body(new PostResponse().convert(postService.getPostById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Can not get Post: \nPost not found"))));
    }

    @DeleteMapping(path = "del/{id}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable("id") UUID id){
        return ResponseEntity.ok().body(new PostResponse().convert(postService.deletePost(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Can not delete Post: \nPost not found"))));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable("id") UUID id,@Valid @NonNull @RequestBody Post post){
       return ResponseEntity.ok().body(new PostResponse().convert(postService.updatePost(id, post).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Can not update Post: \nPost not found"))));
    }
    @GetMapping(path = "search/{title}")
    public List<PostResponse> getAllByTitleContaining(@PathVariable("title") String title){ return new PostResponse().convert(postService.getAllByTitleContaining(title)); }

    @RequestMapping(path = "/user/{id}/posts")
    public List<PostResponse> getAllByUser(@PathVariable("id") UUID id){
        return new PostResponse().convert(postService.getAllByUser(userService.findUserById(id).orElseThrow()));
    }

    //Response and Request Classes: ********************************************************************************************************************************************
    @Data
    public static class PostRequest {
        private String title;
        private String content;
        private String user_id;
    }

    @Data
    public static class PostResponse {
        private UUID id;
        private String title;
        private String content;
        private UUID user_id;
        private String user_name;
        private String date;

        public List<PostResponse> convert(List<Post> allByUser) {
            List<PostResponse> postResponses = new ArrayList<>();
            for (Post post : allByUser) {
                PostResponse postResponse = new PostResponse();
                postResponse.setId(post.getId());
                postResponse.setTitle(post.getTitle());
                postResponse.setContent(post.getContent());
                postResponse.setUser_id(post.getUser().getId());
                postResponse.setUser_name(post.getUser().getUsername());
                postResponse.setDate(post.getDate());
                postResponses.add(postResponse);
            }
            return postResponses;
        }

        public PostResponse convert(Post post) {
            PostResponse postResponse = new PostResponse();
            postResponse.setId(post.getId());
            postResponse.setTitle(post.getTitle());
            postResponse.setContent(post.getContent());
            postResponse.setUser_id(post.getUser().getId());
            postResponse.setUser_name(post.getUser().getUsername());
            postResponse.setDate(post.getDate());
            return postResponse;
        }
    }

}
