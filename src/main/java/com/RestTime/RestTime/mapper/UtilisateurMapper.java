package com.RestTime.RestTime.mapper;
import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.RegisterRequest;
import com.RestTime.RestTime.model.entity.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "soldeConges", constant = "0.0")
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "demandes", ignore = true)
    @Mapping(target = "absences", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    Utilisateur toEntity(RegisterRequest dto);


    AuthResponse toAuthResponse(String token);
}
