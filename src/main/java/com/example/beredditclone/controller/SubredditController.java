package com.example.beredditclone.controller;

import com.example.beredditclone.dto.SubredditDto;
import com.example.beredditclone.exceptions.SubredditNotFoundException;
import com.example.beredditclone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {
    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getSubreddit(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubredditDto> updateSubreddit(@PathVariable Long id, @RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.updateSubreddit(id, subredditDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubreddit(@PathVariable Long id) {
        try {
            subredditService.deleteSubreddit(id);
            return ResponseEntity.status(HttpStatus.OK).body("Subreddit with ID " + id + " has been deleted successfully.");
        } catch (SubredditNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the subreddit.");
        }
    }
}
