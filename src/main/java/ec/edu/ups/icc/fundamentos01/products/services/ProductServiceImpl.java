package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.products.dto.*;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // 1. IMPLEMENTACIÓN: findAll() requerido por la interfaz
    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findAll()
                .stream()
                .filter(entity -> !entity.isDeleted())
                .map(this::toResponse)
                .toList();
    }

    // 2. IMPLEMENTACIÓN: findOne(id) requerido por la interfaz
    @Override
    public ProductResponseDto findOne(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        return this.toResponse(entity);
    }

    // 3. IMPLEMENTACIÓN: delete(id) requerido por la interfaz
    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new ConflictException("Product is already deleted");
        }

        entity.setDeleted(true);
        productRepository.save(entity);
    }

    // 4. IMPLEMENTACIÓN: findByUserId(id) sin filtros
    @Override
    public List<ProductResponseDto> findByUserId(Long userId) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        List<ProductEntity> list = productRepository.findByOwner_IdAndDeletedFalse(userId);

        return list
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 5. IMPLEMENTACIÓN: findByCategoryId(id)
    @Override
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        return productRepository.findByCategory_IdAndDeletedFalse(categoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 6. IMPLEMENTACIÓN: create(dto) con validaciones completas de negocio
    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        UserEntity owner = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        if (productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).isPresent()) {
            throw new ConflictException("Product name already registered");
        }

        ProductEntity entity = new ProductEntity();

        entity.setName(dto.getName());
        entity.setProductName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setOwner(owner);
        entity.setCategory(category);

        ProductEntity savedEntity = productRepository.save(entity);
        return this.toResponse(savedEntity);
    }

    // 7. IMPLEMENTACIÓN: update(id, dto) completo
    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setCategory(category);

        ProductEntity savedEntity = productRepository.save(entity);
        return this.toResponse(savedEntity);
    }

    // 8. IMPLEMENTACIÓN: partialUpdate(id, dto) dinámico
    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }

        if (dto.getStock() != null) {
            entity.setStock(dto.getStock());
        }

        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            if (category.isDeleted()) {
                throw new NotFoundException("Category not found");
            }

            entity.setCategory(category);
        }

        ProductEntity savedEntity = productRepository.save(entity);
        return this.toResponse(savedEntity);
    }

    // 9. IMPLEMENTACIÓN: Búsqueda avanzada de productos por usuario con filtros opcionales
    @Override
    public List<ProductResponseDto> findByUserIdWithFilters(
            Long userId,
            ProductFilterByUserDto filters
    ) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        // Ejecuta tus reglas de negocio para los filtros pasados
        validateUserFilters(filters);

        String name = normalizeName(filters.getName());

        return productRepository.findByOwnerIdWithFilters(
                        userId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getCategoryId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================================
    // MÉTODO HELPER DE MAPEADO DE RESPUESTA
    // ==========================================
    private ProductResponseDto toResponse(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Mapeo anidado exacto para UserSummaryDto
        ProductResponseDto.UserSummaryDto ownerDto = new ProductResponseDto.UserSummaryDto();
        ownerDto.setId(entity.getOwner().getId());
        ownerDto.setName(entity.getOwner().getName());
        ownerDto.setEmail(entity.getOwner().getEmail());
        dto.setOwner(ownerDto);

        // Mapeo anidado exacto para CategorySummaryDto
        ProductResponseDto.CategorySummaryDto categoryDto = new ProductResponseDto.CategorySummaryDto();
        categoryDto.setId(entity.getCategory().getId());
        categoryDto.setName(entity.getCategory().getName());
        categoryDto.setDescription(entity.getCategory().getDescription());
        dto.setCategory(categoryDto);

        return dto;
    }

    // ==========================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ==========================================

    /*
     * Valida reglas de negocio relacionadas con filtros.
     */
    private void validateUserFilters(ProductFilterByUserDto filters) {
        if (filters == null) {
            return;
        }

        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }

        if (filters.getCategoryId() != null &&
                !categoryRepository.existsByIdAndDeletedFalse(filters.getCategoryId())) {
            throw new NotFoundException("Category not found");
        }
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return name.trim();
    }


}