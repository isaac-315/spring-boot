package ec.edu.ups.icc.fundamentos01.categories.entity;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    /*
     * CORREGIDO: Relación inversa Uno a Muchos con productos.
     * * mappedBy apunta a "category" (en singular), que es el nombre exacto
     * del atributo dentro de tu clase ProductEntity.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    public CategoryEntity() {
    }

    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }
}