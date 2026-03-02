package com.RestTime.RestTime.mapper;
import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.model.entity.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    AuthResponse toAuthResponse(String token);
}
