package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.CreateUserRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.model.entity.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    AuthResponse toAuthResponse(String token);

    @Mapping(source = "service.nom", target = "nomService")
    UserResponseDTO toUserResponseDTO(Utilisateur utilisateur);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "demandes", ignore = true)
    @Mapping(target = "absences", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    Utilisateur toEntity(CreateUserRequestDTO request);
}