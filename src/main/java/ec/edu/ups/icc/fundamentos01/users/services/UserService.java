package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.users.dto.*;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();

    UserResponseDto findOne(Long id);

    UserResponseDto create(CreateUserDto dto);

    UserResponseDto update(Long id, UpdateUserDto dto);

    UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto);

    void delete(Long id);

    void changePassword(Long id, ChangePasswordDto dto);
}
