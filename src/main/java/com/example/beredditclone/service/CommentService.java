package com.example.beredditclone.service;

import com.example.beredditclone.dto.CommentsDto;
import com.example.beredditclone.exceptions.PostNotFoundException;
import com.example.beredditclone.mapper.CommentMapper;
import com.example.beredditclone.model.Comment;
import com.example.beredditclone.model.NotificationEmail;
import com.example.beredditclone.model.Post;
import com.example.beredditclone.model.User;
import com.example.beredditclone.repository.CommentRepository;
import com.example.beredditclone.repository.PostRepository;
import com.example.beredditclone.repository.SubredditRepository;
import com.example.beredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        System.out.println("current user: " + authService.getCurrentUser().getUsername());
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment o your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).toList();
    }

    public List<CommentsDto> getAllCommentForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .toList();
    }
}
