package com.example.beredditclone.controller;

import com.example.beredditclone.dto.PostRequest;
import com.example.beredditclone.dto.PostResponse;
import com.example.beredditclone.exceptions.PostNotFoundException;
import com.example.beredditclone.exceptions.SubredditNotFoundException;
import com.example.beredditclone.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping(params = "subredditId")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@RequestParam Long subredditId) {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(subredditId));
    }

    @GetMapping(params = "username")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@RequestParam String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostRequest> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        return status(HttpStatus.OK).body(postService.updatePost(id, postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.status(HttpStatus.OK).body("Post with ID " + id + " has been deleted successfully.");
        } catch (PostNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the post.");
        }
    }
}
