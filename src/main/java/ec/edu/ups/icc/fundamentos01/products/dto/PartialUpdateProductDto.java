package ec.edu.ups.icc.fundamentos01.products.dto;

public class PartialUpdateProductDto {

    private String productName;
    private Double price; // ¡Perfecto!

    public PartialUpdateProductDto() {
    }

    public PartialUpdateProductDto(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Corregido: Ahora devuelve Double objeto
    public Double getPrice() {
        return price;
    }

    // Corregido: Ahora recibe Double objeto
    public void setPrice(Double price) {
        this.price = price;
    }
}