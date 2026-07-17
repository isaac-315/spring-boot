package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.core.dto.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation; // Importante
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Productos",
        description = "Gestión de productos con paginación, roles y ownership"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    // --- MÉTODOS DE LECTURA ---

    @Operation(summary = "Listar todos los productos", description = "Obtiene una lista completa de productos. Requiere rol de administrador.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Listar mis productos", description = "Obtiene los productos creados por el usuario autenticado con paginación.")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Slice<ProductResponseDto>> getMyProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(service.getMyProducts(currentUser.getId(), pageable));
    }

    @Operation(summary = "Obtener producto por ID", description = "Recupera los detalles de un producto específico mediante su ID.")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @Operation(summary = "Listar productos por usuario", description = "Busca todos los productos asociados a un ID de usuario específico.")
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public List<ProductResponseDto> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @Operation(summary = "Listar productos por categoría", description = "Busca todos los productos que pertenecen a una categoría específica.")
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public List<ProductResponseDto> findByCategoryId(@PathVariable Long categoryId) {
        return service.findByCategoryId(categoryId);
    }

    @Operation(summary = "Listar productos paginados", description = "Retorna una página completa de productos basada en parámetros de paginación.")
    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public Page<ProductResponseDto> findAllPage(@Valid @ModelAttribute PaginationDto pagination) {
        return service.findAllPage(pagination);
    }

    @Operation(summary = "Listar productos en slice", description = "Retorna una porción (slice) de productos, ideal para carga infinita.")
    @GetMapping("/slice")
    @PreAuthorize("isAuthenticated()")
    public Slice<ProductResponseDto> findAllSlice(@Valid @ModelAttribute PaginationDto pagination) {
        return service.findAllSlice(pagination);
    }

    // --- MÉTODOS DE ESCRITURA ---

    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(
            @Valid @RequestBody CreateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.create(dto, currentUser);
    }

    @Operation(summary = "Actualizar producto completo", description = "Reemplaza todos los datos de un producto existente.")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ProductResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.update(id, dto, currentUser);
    }

    @Operation(summary = "Actualizar producto parcialmente", description = "Actualiza solo los campos enviados en la solicitud para un producto existente.")
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ProductResponseDto partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.partialUpdate(id, dto, currentUser);
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema mediante su ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        service.delete(id, currentUser);
    }
}