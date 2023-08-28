package com.example.beredditclone.repository;

import com.example.beredditclone.model.Comment;
import com.example.beredditclone.model.Post;
import com.example.beredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
