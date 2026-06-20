package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dto.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UserResponseDto;

import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
     *
     * GET /users
     */
    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    /*
     * Endpoint para buscar un usuario por id.
     *
     * GET /users/{id}
     */
    @GetMapping("/{id}")
    public Object findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    /*
     * Endpoint para crear un nuevo usuario.
     *
     * POST /users
     */
    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    /*
     * Endpoint para actualizar completamente un usuario.
     *
     * PUT /users/{id}
     */
    @PutMapping("/{id}")
    public Object update(
            @PathVariable Long id,
            @RequestBody UpdateUserDto dto
    ) {
        return service.update(id, dto);
    }

    /*
     * Endpoint para actualizar parcialmente un usuario.
     *
     * PATCH /users/{id}
     */
    @PatchMapping("/{id}")
    public Object partialUpdate(
            @PathVariable Long id,
            @RequestBody PartialUpdateUserDto dto
    ) {
        return service.partialUpdate(id, dto);
    }

    /*
     * Endpoint para eliminar un usuario.
     *
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}