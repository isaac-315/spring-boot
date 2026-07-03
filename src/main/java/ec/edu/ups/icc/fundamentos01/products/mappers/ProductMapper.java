package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.categories.dto.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.users.dto.UserResponseDto;

import java.util.stream.Collectors;

public class ProductMapper {

    // 1. Usado en Service.create()
    public static ProductModel toModelFromDTO(CreateProductDto dto) {
        if (dto == null) return null;

        ProductModel model = new ProductModel();
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setStock(dto.getStock());
        // Aquí no seteamos categorías porque el Service lo maneja manualmente
        return model;
    }

    // 2. Usado en Service.findAll(), findOne(), etc. (La firma que el Service espera)
    public static ProductResponseDto toResponse(ProductEntity entity) {
        if (entity == null) return null;

        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Mapeo del Owner (UserEntity -> UserResponseDto)
        if (entity.getOwner() != null) {
            UserResponseDto userDto = new UserResponseDto();
            userDto.setId(entity.getOwner().getId());
            userDto.setName(entity.getOwner().getName());
            userDto.setEmail(entity.getOwner().getEmail());
            dto.setOwner(userDto);
        }

        // Mapeo de Categorías (Set<CategoryEntity> -> List<CategoryResponseDto>)
        if (entity.getCategories() != null && !entity.getCategories().isEmpty()) {
            dto.setCategories(entity.getCategories().stream()
                    .map(c -> {
                        CategoryResponseDto catDto = new CategoryResponseDto();
                        catDto.setId(c.getId());
                        catDto.setName(c.getName());
                        catDto.setDescription(c.getDescription());
                        return catDto;
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}