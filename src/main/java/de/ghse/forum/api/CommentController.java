package de.ghse.forum.api;

import de.ghse.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @Autowired public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
}
