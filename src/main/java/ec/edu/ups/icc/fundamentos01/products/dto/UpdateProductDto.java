package ec.edu.ups.icc.fundamentos01.products.dto;

public class UpdateProductDto {

    private String productName;
    private double price;

    public UpdateProductDto() {
    }

    public UpdateProductDto(String productName, double price) {
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
