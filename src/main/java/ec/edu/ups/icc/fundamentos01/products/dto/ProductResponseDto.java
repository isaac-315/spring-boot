package ec.edu.ups.icc.fundamentos01.products.dto;

import java.time.LocalDateTime;

public class ProductResponseDto {

    private Long id;
    private String name;
    private double price;
    private Integer stock;
    private UserSummaryDto owner;       // CORREGIDO: Ahora usa la clase interna
    private CategorySummaryDto category; // CORREGIDO: Ahora usa la clase interna
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ==========================================
    // CLASES INTERNAS ESTÁTICAS (REQUERIDAS)
    // ==========================================
    public static class UserSummaryDto {
        private Long id;
        private String name;
        private String email;

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class CategorySummaryDto {
        private Long id;
        private String name;
        private String description;

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // ==========================================
    // GETTERS Y SETTERS DE PRODUCTRESPONSEDTO
    // ==========================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public UserSummaryDto getOwner() { return owner; }
    public void setOwner(UserSummaryDto owner) { this.owner = owner; }

    public CategorySummaryDto getCategory() { return category; }
    public void setCategory(CategorySummaryDto category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}