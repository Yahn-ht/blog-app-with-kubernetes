package com.blog.blogapp.controllers;

import com.blog.blogapp.consts.Consts;
import com.blog.blogapp.dtos.UpdatePasswordDto;
import com.blog.blogapp.dtos.UpdateProfileDto;
import com.blog.blogapp.dtos.UserDto;
import com.blog.blogapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "users" )
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(@RequestBody @Valid UpdateProfileDto profileDto, Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(userService.updateProfile(email,profileDto), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault Pageable pageable){
        return new ResponseEntity<>(userService.getAllUsers(pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(Authentication authentication, UpdatePasswordDto dto) {
        String email = authentication.getName();
        userService.updatePassword(email,dto);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

}
