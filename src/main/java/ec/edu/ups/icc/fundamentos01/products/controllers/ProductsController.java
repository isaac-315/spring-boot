package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
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
    public ProductResponseDto findOne(@PathVariable Long id) { // Corregido: Object -> ProductResponseDto
        return service.findOne(id);
    }

    /*
     * Endpoint para crear un nuevo producto.
     * POST /products
     */
    @PostMapping
    public ProductResponseDto create(@RequestBody CreateProductDto dto) {
        return service.create(dto);
    }

    /*
     * Endpoint para actualizar completamente un producto.
     * PUT /products/{id}
     */
    @PutMapping("/{id}")
    public ProductResponseDto update( // Corregido: Object -> ProductResponseDto
                                      @PathVariable Long id,
                                      @RequestBody UpdateProductDto dto
    ) {
        return service.update(id, dto);
    }

    /*
     * Endpoint para actualizar parcialmente un producto.
     * PATCH /products/{id}
     */
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate( // Corregido: Object -> ProductResponseDto
                                             @PathVariable Long id,
                                             @RequestBody PartialUpdateProductDto dto
    ) {
        return service.partialUpdate(id, dto);
    }

    /*
     * Endpoint para eliminar un producto (Borrado lógico).
     * DELETE /products/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { // Corregido: void en lugar de Object, sin return
        service.delete(id);
    }
}