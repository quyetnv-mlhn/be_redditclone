package com.example.beredditclone.service;

import com.example.beredditclone.dto.PostRequest;
import com.example.beredditclone.dto.PostResponse;
import com.example.beredditclone.exceptions.PostNotFoundException;
import com.example.beredditclone.exceptions.SubredditNotFoundException;
import com.example.beredditclone.mapper.PostMapper;
import com.example.beredditclone.model.*;
import com.example.beredditclone.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;


    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        User currentUser = authService.getCurrentUser();
        postRepository.save(postMapper.map(postRequest, subreddit, currentUser));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public PostRequest updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + id));
        postRequest.setPostId(id);
        post.setPostName(postRequest.getPostName());
        post.setUrl(postRequest.getUrl());
        post.setDescription(postRequest.getDescription());
        postRepository.save(post);
        return postRequest;
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + id));
        List<Vote> votes = voteRepository.findByPost(post);
        List<Comment> comments = commentRepository.findByPost(post);
        commentRepository.deleteAll(comments);
        voteRepository.deleteAll(votes);
        postRepository.delete(post);
    }
}
