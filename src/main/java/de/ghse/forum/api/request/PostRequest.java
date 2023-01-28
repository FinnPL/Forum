package de.ghse.forum.api.request;

import de.ghse.forum.api.PostController;
import de.ghse.forum.model.Post;
import lombok.Data;

/**
 * <pre>
 * PostRequest Class for API Requests
 * </pre>
 *
 * @see Post
 * @see PostController
 * @since 1.0
 */
@Data
public class PostRequest {
    private String title;
    private String content;
    private String user_id;
}
