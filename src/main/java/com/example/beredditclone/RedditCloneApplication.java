package com.example.beredditclone;

import com.example.beredditclone.model.User;
import com.example.beredditclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.time.Instant;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class RedditCloneApplication {

    public static void main(String[] args) {

        SpringApplication.run(RedditCloneApplication.class, args);
    }
}
