package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductMapper {

    // 1. De DTO de creación a Modelo de negocio
    public static ProductModel toModelFromDTO(CreateProductDto dto) {
        ProductModel model = new ProductModel();

        model.setProductName(dto.getProductName());
        model.setPrice(dto.getPrice());

        // Si tu ProductModel hereda de un BaseModel o maneja auditoría:
        // model.setCreatedAt(LocalDateTime.now());

        return model;
    }

    // 2. De Entidad (Base de datos / Docker) a Modelo de negocio (Convierte BigDecimal a double)
    public static ProductModel toModelFromEntity(ProductEntity entity) {
        ProductModel model = new ProductModel();

        model.setId(entity.getId());
        model.setProductName(entity.getProductName());

        // Conversión segura: Si el precio en la BD no es nulo, lo pasa a double
        if (entity.getPrice() != null) {
            model.setPrice(entity.getPrice().doubleValue());
        }

        // Si tu ProductModel maneja los campos heredados de BaseEntity:
        // model.setCreatedAt(entity.getCreatedAt());
        // model.setUpdatedAt(entity.getUpdatedAt());
        // model.setDeleted(entity.isDeleted());

        return model;
    }

    // 3. De Modelo de negocio a Entidad de base de datos (Convierte double a BigDecimal)
    public static ProductEntity toEntityFromModel(ProductModel model) {
        ProductEntity entity = new ProductEntity();

        entity.setId(model.getId());
        entity.setProductName(model.getProductName());

        // Conversión segura: Pasa el double del modelo al BigDecimal de la entidad
        entity.setPrice(BigDecimal.valueOf(model.getPrice()));

        return entity;
    }

    // 4. De Modelo de negocio a DTO de Respuesta (Hacia Bruno / Angular)
    public static ProductResponseDto toResponse(ProductModel model) {
        ProductResponseDto response = new ProductResponseDto();

        response.setId(model.getId());
        response.setProductName(model.getProductName());
        response.setPrice(model.getPrice());

        return response;
    }
}