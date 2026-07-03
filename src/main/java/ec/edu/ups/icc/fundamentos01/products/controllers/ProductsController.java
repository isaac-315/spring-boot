package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping("/user/{userId}")
    public List<ProductResponseDto> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> findByCategoryId(@PathVariable Long categoryId) {
        return service.findByCategoryId(categoryId);
    }

    @PostMapping
    public ProductResponseDto create(@Valid @RequestBody CreateProductDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable Long id, @Valid @RequestBody UpdateProductDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(@PathVariable Long id, @Valid @RequestBody PartialUpdateProductDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}