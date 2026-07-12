package com.project.LinkedIn.user_service.controller;

import com.project.LinkedIn.user_service.dto.LoginRequestDto;
import com.project.LinkedIn.user_service.dto.SignupRequestDto;
import com.project.LinkedIn.user_service.dto.UserDto;
import com.project.LinkedIn.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authservice;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        // Implement signup logic here
        UserDto userDto = authservice.signup(signupRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        // Implement signup logic here
        String token = authservice.login(loginRequestDto);
        return ResponseEntity.ok(token);
    }
}

