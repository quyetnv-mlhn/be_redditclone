package com.example.beredditclone.service;

import com.example.beredditclone.dto.SubredditDto;
import com.example.beredditclone.exceptions.SpringRedditException;
import com.example.beredditclone.exceptions.SubredditNotFoundException;
import com.example.beredditclone.mapper.SubredditMapper;
import com.example.beredditclone.model.Subreddit;
import com.example.beredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;
    }


    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Transactional
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with id: " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

    @Transactional
    public SubredditDto updateSubreddit(Long id, SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("No subreddit found with id: " + id));
        subredditDto.setId(id);
        subreddit.setName(subredditDto.getName());
        subreddit.setDescription(subredditDto.getDescription());
        subredditRepository.save(subreddit);
        return subredditDto;
    }

    @Transactional
    public void deleteSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("No subreddit found with id: " + id));
        subredditRepository.delete(subreddit);
    }
}
