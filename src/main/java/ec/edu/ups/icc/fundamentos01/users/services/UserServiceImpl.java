package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.users.dto.*;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.isDeleted())
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDto findOne(Long id) {
        UserEntity entity = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        return UserMapper.toResponse(
                UserMapper.toModelFromEntity(entity)
        );
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {
        userRepository.findByEmail(dto.getEmail())
                .filter(user -> !user.isDeleted())
                .ifPresent(user -> {
                    throw new ConflictException("Email already registered");
                });

        UserEntity entity = UserMapper.toEntityFromModel(
                UserMapper.toModelFromDTO(dto)
        );

        UserEntity saved = userRepository.save(entity);

        return UserMapper.toResponse(
                UserMapper.toModelFromEntity(saved)
        );
    }

    @Override
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        UserEntity entity = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        // Usamos el método estático del Mapper para actualizar name, email y password con "HASH_"
        UserMapper.updateEntityFromDto(dto, entity);

        UserEntity saved = userRepository.save(entity);

        return UserMapper.toResponse(
                UserMapper.toModelFromEntity(saved)
        );
    }

    @Override
    public UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto) {
        UserEntity entity = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        // Usamos el método estático del Mapper para manejar los nulos y el "HASH_" de la contraseña
        UserMapper.partialUpdateEntityFromDto(dto, entity);

        UserEntity saved = userRepository.save(entity);

        return UserMapper.toResponse(
                UserMapper.toModelFromEntity(saved)
        );
    }

    @Override
    public void delete(Long id) {
        UserEntity entity = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        entity.setDeleted(true);
        userRepository.save(entity);
    }

    @Override
    public void changePassword(Long id, ChangePasswordDto dto) {
        UserEntity entity = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String expectedHash = "HASH_" + dto.getCurrentPassword();
        if (!expectedHash.equals(entity.getPasswordHash())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }

        entity.setPasswordHash("HASH_" + dto.getNewPassword());
        userRepository.save(entity);
    }
}