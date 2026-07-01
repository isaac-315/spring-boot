package ec.edu.ups.icc.fundamentos01.categories.service;

import ec.edu.ups.icc.fundamentos01.categories.dto.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dto.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.dto.UpdateCategoryDto;

import java.util.List;

/*
 * Servicio que define las operaciones disponibles
 * para la gestión de categorías.
 */
public interface CategoryService {

    List<CategoryResponseDto> findAll();

    CategoryResponseDto findOne(Long id);

    CategoryResponseDto create(CreateCategoryDto dto);

    CategoryResponseDto update(Long id, UpdateCategoryDto dto);

    void delete(Long id);
}
