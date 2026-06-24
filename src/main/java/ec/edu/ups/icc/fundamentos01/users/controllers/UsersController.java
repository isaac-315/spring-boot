package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dto.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    /*
     * Endpoint para listar todos los usuarios.
     * GET /users
     */
    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    /*
     * Endpoint para buscar un usuario por id.
     * GET /users/{id}
     */
    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable Long id) { // Corregido: Object -> UserResponseDto
        return service.findOne(id);
    }

    /*
     * Endpoint para crear un nuevo usuario.
     * POST /users
     */
    @PostMapping
    public UserResponseDto create(@Valid @RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    /*
     * Endpoint para actualizar completamente un usuario.
     * PUT /users/{id}
     */
    @PutMapping("/{id}")
    public UserResponseDto update( // Corregido: Object -> UserResponseDto
                                   @PathVariable Long id,
                                   @RequestBody UpdateUserDto dto
    ) {
        return service.update(id, dto);
    }

    /*
     * Endpoint para actualizar parcialmente un usuario.
     * PATCH /users/{id}
     */
    @PatchMapping("/{id}")
    public UserResponseDto partialUpdate( // Corregido: Object -> UserResponseDto
                                          @PathVariable Long id,
                                          @RequestBody PartialUpdateUserDto dto
    ) {
        return service.partialUpdate(id, dto);
    }

    /*
     * Endpoint para eliminar un usuario.
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { // Corregido: void en lugar de Object, sin return
        service.delete(id);
    }
}