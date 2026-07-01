package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.users.dto.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dto.CategoryResponseDto;

public class ProductMapper {

    public static ProductModel toModelFromDTO(CreateProductDto dto) {
        ProductModel model = new ProductModel();
        model.setProductName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setStock(dto.getStock());
        model.setUserId(dto.getUserId());
        model.setCategoryId(dto.getCategoryId());
        return model;
    }

    public static ProductModel toModelFromEntity(ProductEntity entity) {
        ProductModel model = new ProductModel();
        model.setId(entity.getId());
        model.setProductName(entity.getName());
        model.setPrice(entity.getPrice());
        model.setStock(entity.getStock());

        if (entity.getOwner() != null) model.setUserId(entity.getOwner().getId());
        if (entity.getCategory() != null) model.setCategoryId(entity.getCategory().getId());

        return model;
    }

    // MAPEO FINAL DE RESPUESTA CON OBJETOS ANIDADOS
    // Reemplaza el toResponse viejo por este que pasaste de tu guía:




    /*
     * Convierte ProductEntity a ProductResponseDto.
     *
     * Incluye datos anidados del usuario propietario y de la categoría.
     */
    private ProductResponseDto toResponse(ProductEntity entity) {

        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        ProductResponseDto.UserSummaryDto ownerDto = new ProductResponseDto.UserSummaryDto();

        ownerDto.setId(entity.getOwner().getId());
        ownerDto.setName(entity.getOwner().getName());
        ownerDto.setEmail(entity.getOwner().getEmail());

        dto.setOwner(ownerDto);

        ProductResponseDto.CategorySummaryDto categoryDto = new ProductResponseDto.CategorySummaryDto();

        categoryDto.setId(entity.getCategory().getId());
        categoryDto.setName(entity.getCategory().getName());
        categoryDto.setDescription(entity.getCategory().getDescription());

        dto.setCategory(categoryDto);

        return dto;
    }
}