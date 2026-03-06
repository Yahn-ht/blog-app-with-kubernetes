package com.blog.blogapp.controllers;

import com.blog.blogapp.consts.Consts;
import com.blog.blogapp.dtos.LoginRequest;
import com.blog.blogapp.dtos.LoginResponse;
import com.blog.blogapp.dtos.RegisterRequestDto;
import com.blog.blogapp.dtos.UserDto;
import com.blog.blogapp.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "auth" )
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterRequestDto dto){
        UserDto userDto = authService.register(dto);
        return ResponseEntity
                .created(URI.create(Consts.API_BASE + Consts.API_VERSION + "auth/" +userDto.id() ))
                .body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}
