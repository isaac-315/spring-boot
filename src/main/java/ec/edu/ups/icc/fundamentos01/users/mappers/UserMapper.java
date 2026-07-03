package ec.edu.ups.icc.fundamentos01.users.mappers;

import ec.edu.ups.icc.fundamentos01.users.dto.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserModel toModelFromDTO(CreateUserDto dto) {
        UserModel model = new UserModel();
        model.setName(dto.getName());
        model.setEmail(dto.getEmail());
        model.setPassword(dto.getPassword());
        model.setPasswordHash("HASH_" + dto.getPassword());
        model.setCreatedAt(LocalDateTime.now());
        return model;
    }

    public static UserModel toModelFromEntity(UserEntity entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setPasswordHash(entity.getPasswordHash());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setDeleted(entity.isDeleted());
        return model;
    }

    public static UserEntity toEntityFromModel(UserModel model) {
        UserEntity entity = new UserEntity();
        entity.setId(model.getId());
        entity.setEmail(model.getEmail());
        entity.setPasswordHash(model.getPasswordHash());
        return entity;
    }

    public static UserResponseDto toResponse(UserModel model) {
        UserResponseDto response = new UserResponseDto();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setEmail(model.getEmail());
        return response;
    }

    // Retorna UserEntity para poder meterlo directo en el .save() del servicio
    public static UserEntity updateEntityFromDto(UpdateUserDto dto, UserEntity entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPasswordHash("HASH_" + dto.getPassword());
        }
        return entity;
    }

    // Retorna UserEntity manejando nulos y añadiendo HASH_
    public static UserEntity partialUpdateEntityFromDto(PartialUpdateUserDto dto, UserEntity entity) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPasswordHash("HASH_" + dto.getPassword());
        }
        return entity;
    }
}