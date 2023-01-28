package de.ghse.forum.service;

import de.ghse.forum.model.Comment;
import de.ghse.forum.repository.CommentRepository;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
  @Value("${page.size}")
  private int PAGE_SIZE;

  private final CommentRepository commentRepository;

  public void addComment(Comment comment) {
    commentRepository.save(comment);
  }

  public List<Comment> findCommentByPostByPage(UUID post, int page) {
    return commentRepository.findCommentByPostByPage(post, PageRequest.of(page, PAGE_SIZE));
  }
}
