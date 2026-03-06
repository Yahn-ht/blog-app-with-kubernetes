package com.blog.blogapp.mappers;

import com.blog.blogapp.domains.User;
import com.blog.blogapp.dtos.RegisterRequestDto;
import com.blog.blogapp.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserDto dto);
    UserDto toDto(User user);

    List<User> toEntities(List<UserDto> dtos);
    List<UserDto> toDtos(List<User> users);

    User fromRegisterDtoToUser(RegisterRequestDto registerRequestDto);
}
