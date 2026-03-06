package com.blog.blogapp.services;

import com.blog.blogapp.dtos.UpdatePasswordDto;
import com.blog.blogapp.dtos.UpdateProfileDto;
import com.blog.blogapp.dtos.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long id);
    void deleteUser(Long id);
    UserDto updateProfile(String email , UpdateProfileDto dto);
    Page<UserDto> getAllUsers(Pageable pageable);
    void updatePassword(String email,UpdatePasswordDto dto);
}
