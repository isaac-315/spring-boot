package ec.edu.ups.icc.fundamentos01.products.dto;

import ec.edu.ups.icc.fundamentos01.categories.dto.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO de respuesta que contiene la información detallada de un producto")
public class ProductResponseDto {

    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre comercial del producto", example = "Laptop Gamer")
    private String name;

    @Schema(description = "Precio unitario del producto", example = "1200.50")
    private Double price;

    @Schema(description = "Cantidad de existencias disponibles", example = "50")
    private Integer stock;

    @Schema(description = "Información del usuario propietario del producto")
    private UserResponseDto owner;

    @Schema(description = "Lista de categorías asociadas al producto")
    private List<CategoryResponseDto> categories;

    @Schema(description = "Fecha y hora de creación", example = "2026-06-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-06-01T12:30:00")
    private LocalDateTime updatedAt;

    public ProductResponseDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public UserResponseDto getOwner() { return owner; }
    public void setOwner(UserResponseDto owner) { this.owner = owner; }

    public List<CategoryResponseDto> getCategories() { return categories; }
    public void setCategories(List<CategoryResponseDto> categories) { this.categories = categories; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}