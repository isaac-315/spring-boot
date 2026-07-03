package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dto.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService service;

    public ProductsController(ProductService service){
        this.service = service;
    }

    /*
     * Endpoint para listar todos los productos.
     * GET /products
     */
    @GetMapping
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    /*
     * Endpoint para buscar un producto por id.
     * GET /products/{id}
     */
    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    /*
     * CAMBIO: Endpoint para crear un nuevo producto usando datos de formulario.
     * POST /products
     */
    @PostMapping
    public ProductResponseDto create(@Valid @ModelAttribute CreateProductDto dto) { // Mapea campos individuales de formulario
        return service.create(dto);
    }

    /*
     * CAMBIO: Endpoint para actualizar completamente un producto usando datos de formulario.
     * PUT /products/{id}
     */
    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateProductDto dto // Mapea campos individuales de formulario
    ) {
        return service.update(id, dto);
    }

    /*
     * CAMBIO: Endpoint para actualizar parcialmente un producto usando datos de formulario.
     * PATCH /products/{id}
     */
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(
            @PathVariable Long id,
            @Valid @ModelAttribute PartialUpdateProductDto dto // Mapea campos individuales de formulario
    ) {
        return service.partialUpdate(id, dto);
    }

    /*
     * Endpoint para eliminar un producto (Borrado lógico).
     * DELETE /products/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /*
     * Endpoint para buscar productos por id de categoría.
     * GET /products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> findByCategoryId(@PathVariable Long categoryId) {
        return service.findByCategoryId(categoryId);
    }

    /*
     * Endpoint para buscar productos por ID de usuario.
     * GET /products/users/{id}
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUserId(@PathVariable("id") Long userId) {
        List<ProductResponseDto> products = service.findByUserId(userId);
        return ResponseEntity.ok(products);
    }

    /*
     * PASO 7: Buscar productos de un usuario aplicando filtros avanzados
     * GET /products/users/{id}/filter
     */
    @GetMapping("/users/{id}/filter")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUserIdWithFilters(
            @PathVariable("id") Long userId,
            @Valid @ModelAttribute ProductFilterByUserDto filters
    ) {
        List<ProductResponseDto> products = service.findByUserIdWithFilters(userId, filters);
        return ResponseEntity.ok(products);
    }
}