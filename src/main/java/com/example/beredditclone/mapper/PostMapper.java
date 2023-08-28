package com.example.beredditclone.mapper;

import com.example.beredditclone.dto.PostRequest;
import com.example.beredditclone.dto.PostResponse;
import com.example.beredditclone.model.*;
import com.example.beredditclone.repository.CommentRepository;
import com.example.beredditclone.repository.VoteRepository;
import com.example.beredditclone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.util.Optional;

import static com.example.beredditclone.model.VoteType.DOWNVOTE;
import static com.example.beredditclone.model.VoteType.UPVOTE;


@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);


    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {

        return calculateTimeDifference(post) + " days ago";
    }

    public long calculateTimeDifference(Post post) {
        Instant currentTime = Instant.now();
        Instant postCreatedTime = post.getCreatedDate();

        LocalDate currentDate = currentTime.atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate postCreatedDate = postCreatedTime.atZone(ZoneOffset.UTC).toLocalDate();

        Period period = Period.between(postCreatedDate, currentDate);

        return period.getDays();
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}
