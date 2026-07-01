package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid; // ¡Anotación necesaria importada!
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
     * Endpoint para crear un nuevo producto.
     * POST /products
     */
    @PostMapping
    public ProductResponseDto create(@Valid @RequestBody CreateProductDto dto) { // Activado con @Valid
        return service.create(dto);
    }

    /*
     * Endpoint para actualizar completamente un producto.
     * PUT /products/{id}
     */
    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto dto // Activado con @Valid
    ) {
        return service.update(id, dto);
    }

    /*
     * Endpoint para actualizar parcialmente un producto.
     * PATCH /products/{id}
     */
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateProductDto dto // Activado con @Valid
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

    @GetMapping("/user/{userId}")
    public List<ProductResponseDto> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    /*
     * Endpoint para buscar productos por id de categoría.
     *
     * GET /products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> findByCategoryId(@PathVariable Long categoryId) {
        return service.findByCategoryId(categoryId);
    }

}