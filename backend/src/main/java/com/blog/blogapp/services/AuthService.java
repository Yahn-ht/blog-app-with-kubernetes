package com.blog.blogapp.services;

import com.blog.blogapp.dtos.LoginRequest;
import com.blog.blogapp.dtos.LoginResponse;
import com.blog.blogapp.dtos.RegisterRequestDto;
import com.blog.blogapp.dtos.UserDto;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserDto register (RegisterRequestDto dto);
}
