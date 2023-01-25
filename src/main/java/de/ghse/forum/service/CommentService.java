package de.ghse.forum.service;

import de.ghse.forum.model.Comment;
import de.ghse.forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getTop20Comments() {
        return commentRepository.findTop20OrderByDateDesc();
    }

    public List<Comment> getTop20CommentsAfter(Timestamp date) {
        return commentRepository.findTop20ByDateAfterOrderByDateDesc(date);
    }

}
