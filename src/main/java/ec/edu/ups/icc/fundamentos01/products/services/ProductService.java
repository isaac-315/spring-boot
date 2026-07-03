package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductFilterByUserDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.UpdateProductDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(Long id);

    ProductResponseDto create(CreateProductDto dto);

    ProductResponseDto update(Long id, UpdateProductDto dto);

    ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto);

    void delete(Long id);

    List<ProductResponseDto> findByUserId(Long userId);

    List<ProductResponseDto> findByCategoryId(Long categoryId);

    List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters);

    List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByUserDto filters);
}