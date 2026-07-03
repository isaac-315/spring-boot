package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.products.dto.ProductFilterByUserDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import ec.edu.ups.icc.fundamentos01.users.dto.*;
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
    public UserResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDto dto // <-- ¡Listo!
    ) {
        return service.update(id, dto);
    }

    /*
     * Endpoint para actualizar parcialmente un usuario.
     * PATCH /users/{id}
     */
    @PatchMapping("/{id}")
    public UserResponseDto partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateUserDto dto // <-- ¡También aquí!
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

    /*
     * Endpoint para cambiar la contraseña de un usuario.
     * PATCH /users/{id}/change-password
     */
    @PatchMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDto dto
    ) {
        service.changePassword(id, dto);
    }

    @RestController
    @RequestMapping("/users")
    public class UserProductsController {

        private final ProductService productService;

        public UserProductsController(

                ProductService productService
        ) {
            this.productService = productService;
        }


        /*
         * Endpoint para consultar productos de un usuario.
         *
         * GET /api/users/{id}/products
         * GET /api/users/{id}/products?name=laptop
         * GET /api/users/{id}/products?minPrice=500&maxPrice=1500
         * GET /api/users/{id}/products?categoryId=2
         */
        @GetMapping("/{id}/products")
        public List<ProductResponseDto> findProductsByUser(
                @PathVariable Long id,
                @Valid @ModelAttribute ProductFilterByUserDto filters
        ) {
            return productService.findByUserIdWithFilters(id, filters);
        }
    }
}