package de.ghse.forum.api.response;

import de.ghse.forum.api.PostController;
import de.ghse.forum.model.Post;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * PostResponse Class for API Responses
 * </pre>
 *
 * @see Post
 * @see PostController
 * @since 1.0
 */
@Data
public class PostResponse {
    private String id;
    private String title;
    private String content;
    private String user_id;
    private String user_name;
    private String date;

    /**
     * <pre>
     * Converts a List of Posts to a List of PostResponses
     * </pre>
     *
     * @param posts List of Post Objects
     * @return PostResponse List of PostResponse Objects
     * @see Post
     * @see PostResponse
     * @since 1.0
     */
    public List<PostResponse> convert(List<Post> posts) {
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            PostResponse postResponse = new PostResponse();
            postResponse.setId(String.valueOf(post.getId()));
            postResponse.setTitle(post.getTitle());
            postResponse.setContent(post.getContent());
            postResponse.setUser_id(String.valueOf(post.getUser().getId()));
            postResponse.setUser_name(post.getUser().getUsername());
            postResponse.setDate(String.valueOf(post.getDate()));
            postResponses.add(postResponse);
        }
        return postResponses;
    }

    /**
     * Converts a Post Object to a PostResponse Object
     *
     * @param post Post Object
     * @return PostResponse Object
     * @see Post
     * @see PostResponse
     * @since 1.0
     */
    public PostResponse convert(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(String.valueOf(post.getId()));
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setUser_id(String.valueOf(post.getUser().getId()));
        postResponse.setUser_name(post.getUser().getUsername());
        postResponse.setDate(String.valueOf(post.getDate()));
        return postResponse;
    }
}
