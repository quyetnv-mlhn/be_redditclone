package com.example.beredditclone.repository;

import com.example.beredditclone.model.Post;
import com.example.beredditclone.model.User;
import com.example.beredditclone.model.Vote;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

    List<Vote> findByPost(Post post);
}
