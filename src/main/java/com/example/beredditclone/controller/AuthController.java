package com.example.beredditclone.controller;

import com.example.beredditclone.dto.AuthenticationResponse;
import com.example.beredditclone.dto.LoginRequest;
import com.example.beredditclone.dto.RegisterRequest;
import com.example.beredditclone.model.User;
import com.example.beredditclone.repository.UserRepository;
import com.example.beredditclone.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Getter
    @Autowired
    private UserRepository userRepository;


    @GetMapping("")
    List<User> getAllUser() {
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successfully", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
