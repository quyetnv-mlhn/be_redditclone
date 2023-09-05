package com.example.beredditclone.controller;

import com.example.beredditclone.dto.CommentsDto;
import com.example.beredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Object> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.save(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(postId));

    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName) {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentForUser(userName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
