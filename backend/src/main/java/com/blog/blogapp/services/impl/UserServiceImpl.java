package com.blog.blogapp.services.impl;

import com.blog.blogapp.domains.User;
import com.blog.blogapp.dtos.UpdatePasswordDto;
import com.blog.blogapp.dtos.UpdateProfileDto;
import com.blog.blogapp.dtos.UserDto;
import com.blog.blogapp.exceptions.BadRequestException;
import com.blog.blogapp.exceptions.ResourceNotFoundException;
import com.blog.blogapp.mappers.UserMapper;
import com.blog.blogapp.repositories.UserRepository;
import com.blog.blogapp.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserByEmail(String email) {
        Objects.requireNonNull(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow( () -> new ResourceNotFoundException("user.notfound"));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        Objects.requireNonNull(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        Objects.requireNonNull(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserDto updateProfile(String email, UpdateProfileDto dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));

        if(dto.firstName() != null && !dto.firstName().equals(user.getFirstName())){
            user.setFirstName(dto.firstName());
        }

        if(dto.lastName() != null && !dto.lastName().equals(user.getLastName())){
            user.setLastName(dto.lastName());
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public void updatePassword(String email,UpdatePasswordDto dto) {
        Objects.requireNonNull(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));

        if(dto.oldPassword() == null || !passwordEncoder.matches(dto.oldPassword(), user.getPassword())){
            throw new BadRequestException("user.passwordInvalid");
        }
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

}
