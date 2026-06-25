package ec.edu.ups.icc.fundamentos01.products.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateProductDto {

    // Regla mínima: Obligatorio, 3 a 150 caracteres
    // Regla adicional (2): Evita caracteres especiales o scripts maliciosos
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑüÜ\\-]+$", message = "El nombre no debe contener caracteres especiales")
    private String productName;

    // Regla mínima: Obligatorio, mínimo 0
    // Regla adicional (3): Evita que un número gigantesco rompa los límites de Postgres
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio mínimo debe ser 0.0")
    @DecimalMax(value = "100000.00", message = "El precio no puede exceder los 100,000.00")
    private Double price;


    public CreateProductDto() {
    }

    public CreateProductDto(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}