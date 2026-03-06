package com.blog.blogapp.services.impl;

import com.blog.blogapp.configs.JwtUtils;
import com.blog.blogapp.domains.User;
import com.blog.blogapp.dtos.LoginRequest;
import com.blog.blogapp.dtos.LoginResponse;
import com.blog.blogapp.dtos.RegisterRequestDto;
import com.blog.blogapp.dtos.UserDto;
import com.blog.blogapp.enumeration.Role;
import com.blog.blogapp.exceptions.ResourceAlreadyExistsException;
import com.blog.blogapp.mappers.UserMapper;
import com.blog.blogapp.repositories.UserRepository;
import com.blog.blogapp.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public UserDto register(RegisterRequestDto dto){

        if(userRepository.existsByEmail(dto.email())){
            throw new ResourceAlreadyExistsException("email.exists");
        }

        User user = userMapper.fromRegisterDtoToUser(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){

        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  loginRequest.email(),
                  loginRequest.password()
          )
        );

        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("user.notfound"));

        String jwtToken = jwtUtils.generateToken(user);

        return new LoginResponse(jwtToken);
    }



}
