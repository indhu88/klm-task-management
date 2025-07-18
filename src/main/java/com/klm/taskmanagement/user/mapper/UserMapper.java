package com.klm.taskmanagement.user.mapper;

import com.klm.taskmanagement.user.dto.AuthResponseDto;
import com.klm.taskmanagement.user.dto.UserResponse;
import com.klm.taskmanagement.user.entity.User;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User entity and
 * its corresponding Data Transfer Objects (DTOs).
 *
 * Utilizes MapStruct to generate implementation code at compile time.
 * This interface defines mapping methods for:
 * - Converting a User entity to a UserResponse DTO.
 * - Converting a User entity to an AuthResponseDto DTO.
 *
 * The generated mapper is a Spring component and can be injected
 * wherever needed.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    AuthResponseDto toAuthResponse(User user);
}
