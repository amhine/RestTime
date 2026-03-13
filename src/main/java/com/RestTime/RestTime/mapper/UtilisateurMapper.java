package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.CreateUserRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    AuthResponse toAuthResponse(String token);

    UserResponseDTO toUserResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "demandes", ignore = true)
    @Mapping(target = "absences", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    User toEntity(CreateUserRequestDTO request);
}