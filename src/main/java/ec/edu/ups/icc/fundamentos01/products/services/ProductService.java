package ec.edu.ups.icc.fundamentos01.products.services;


import ec.edu.ups.icc.fundamentos01.core.dto.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dto.*; // Importación masiva de tus DTOs
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(Long id);

    ProductResponseDto create(
            CreateProductDto dto,
            UserDetailsImpl currentUser
    );

    /*
     * Actualiza completamente un producto.
     * Se valida ownership en el servicio.
     */
    ProductResponseDto update(
            Long id,
            UpdateProductDto dto,
            UserDetailsImpl currentUser
    );

    /*
     * Actualiza parcialmente un producto.
     * Se valida ownership en el servicio.
     */
    ProductResponseDto partialUpdate(
            Long id,
            PartialUpdateProductDto dto,
            UserDetailsImpl currentUser
    );

    /*
     * Elimina lógicamente un producto.
     * Se valida ownership en el servicio.
     */
    void delete(
            Long id,
            UserDetailsImpl currentUser
    );

    List<ProductResponseDto> findByUserId(Long userId);

    List<ProductResponseDto> findByCategoryId(Long categoryId);

    List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters);

    List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByUserDto filters);

    Page<ProductResponseDto> findAllPage(PaginationDto pagination);

    Slice<ProductResponseDto> findAllSlice(PaginationDto pagination);

    // Agrega esto en tu ProductService.java
    Slice<ProductResponseDto> getMyProducts(Long userId, Pageable pageable);


}