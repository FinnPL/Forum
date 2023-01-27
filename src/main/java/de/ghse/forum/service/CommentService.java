package de.ghse.forum.service;

import de.ghse.forum.model.Comment;
import de.ghse.forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Value("${page.size}")
    private int PAGE_SIZE;
    @Autowired private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> findCommentByPostByPage(UUID post, int page) {
        return commentRepository.findCommentByPostByPage(post, PageRequest.of(page, PAGE_SIZE));
    }

}
