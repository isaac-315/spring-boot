package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dto.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // Corregido: Se asignaba la variable a sí misma por un typo en el constructor anterior
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new IllegalStateException("Product not found"));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        // 1. DTO -> Modelo
        ProductModel model = ProductMapper.toModelFromDTO(dto);

        // 2. Modelo -> Entidad (aquí se hace la magia de double a BigDecimal)
        ProductEntity entity = ProductMapper.toEntityFromModel(model);

        // 3. Guardar en PostgreSQL de Docker
        ProductEntity savedEntity = productRepository.save(entity);

        // 4. Entidad devuelta -> Modelo -> DTO de respuesta
        ProductModel savedModel = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(savedModel);
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        entity.setProductName(dto.getProductName());
        entity.setPrice(BigDecimal.valueOf(dto.getPrice())); // Conversión a BigDecimal

        ProductEntity savedEntity = productRepository.save(entity);
        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(model);
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        if (dto.getProductName() != null) {
            entity.setProductName(dto.getProductName());
        }

        if (dto.getPrice() != null) {
            entity.setPrice(BigDecimal.valueOf(dto.getPrice())); // Conversión segura si viene el precio
        }

        ProductEntity savedEntity = productRepository.save(entity);
        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(model);
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        // Borrado lógico (Auditoría de tu BaseEntity)
        entity.setDeleted(true);

        productRepository.save(entity);
    }
}