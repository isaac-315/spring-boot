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

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findAll()
                .stream()
                // Validación 2: Filtrar para NO devolver productos eliminados
                .filter(entity -> !entity.isDeleted())
                .map(ProductMapper::toModelFromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        // Opcional: Si buscan un producto eliminado individualmente, lanzamos excepción
        if (entity.isDeleted()) {
            throw new IllegalStateException("Product is deleted and cannot be viewed");
        }

        ProductModel model = ProductMapper.toModelFromEntity(entity);
        return ProductMapper.toResponse(model);
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        ProductModel model = ProductMapper.toModelFromDTO(dto);
        ProductEntity entity = ProductMapper.toEntityFromModel(model);

        // Aseguramos que por defecto no nazca eliminado
        entity.setDeleted(false);

        ProductEntity savedEntity = productRepository.save(entity);
        ProductModel savedModel = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(savedModel);
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        // Validación 1: No actualizar productos eliminados
        if (entity.isDeleted()) {
            throw new IllegalStateException("Cannot update a deleted product");
        }

        entity.setProductName(dto.getProductName());
        entity.setPrice(BigDecimal.valueOf(dto.getPrice()));

        ProductEntity savedEntity = productRepository.save(entity);
        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(model);
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        // Validación 1: No actualizar productos eliminados (aplica también a PATCH)
        if (entity.isDeleted()) {
            throw new IllegalStateException("Cannot update a deleted product");
        }

        if (dto.getProductName() != null) {
            entity.setProductName(dto.getProductName());
        }

        if (dto.getPrice() != null) {
            entity.setPrice(BigDecimal.valueOf(dto.getPrice()));
        }

        ProductEntity savedEntity = productRepository.save(entity);
        ProductModel model = ProductMapper.toModelFromEntity(savedEntity);

        return ProductMapper.toResponse(model);
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        // Validación 3: No eliminar dos veces el mismo producto
        if (entity.isDeleted()) {
            throw new IllegalStateException("Product is already deleted");
        }

        // Borrado lógico
        entity.setDeleted(true);
        productRepository.save(entity);
    }
}