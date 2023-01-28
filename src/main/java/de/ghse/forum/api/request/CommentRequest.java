package de.ghse.forum.api.request;

import lombok.Data;

@Data
public class CommentRequest {
    private String title;
    private String text;
    private String user_id;
    private String post_id;
}
