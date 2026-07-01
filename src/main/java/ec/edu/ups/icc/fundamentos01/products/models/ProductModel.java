package ec.edu.ups.icc.fundamentos01.products.models;

public class ProductModel {

    private Long id;
    private String productName;
    private double price;
    private Integer stock;      // Añadido
    private Long userId;        // Añadido para capturar el propietario
    private Long categoryId;    // Añadido para capturar la categoría

    public ProductModel() {
    }

    public ProductModel(Long id, String productName, double price, Integer stock, Long userId, Long categoryId) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}