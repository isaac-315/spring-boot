package ec.edu.ups.icc.fundamentos01.products.entity;

import ec.edu.ups.icc.fundamentos01.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    // COLUMNA 1: Mapea a la columna física "name"
    @Column(name = "name", nullable = false)
    private String name;

    // COLUMNA 2: Mapea a la columna física "product_name"
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    // Relación con el usuario dueño del producto
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    // CORREGIDO: Cambiado de @ManyToMany a @ManyToOne para alinearse con tu Service y DTOs
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public ProductEntity() {
    }

    // GETTERS Y SETTERS COMPLEMENTARIOS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    // CORREGIDO: Ahora el getter y setter sí tienen un atributo real al cual apuntar
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}