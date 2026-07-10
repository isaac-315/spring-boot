package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.dto.PaginationDto;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dto.*;
import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
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

    // --- MÉTODOS DE CONSULTA (Read Only) ---

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {
        return productRepository.findByDeletedFalse().stream()
                .map(ProductMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAllPage(PaginationDto pagination) {
        return productRepository.findActivePage(createPageable(pagination))
                .map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> findAllSlice(PaginationDto pagination) {
        return productRepository.findActiveSlice(createPageable(pagination))
                .map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findOne(Long id) {
        return ProductMapper.toResponse(findEntityOrThrow(id));
    }

    // --- MÉTODOS DE CREACIÓN, EDICIÓN Y ELIMINACIÓN ---

    @Override
    public ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser) {
        UserEntity owner = findCurrentUserEntity(currentUser);

        // Nueva validación de nombre
        validateProductNameForCreate(dto.getName());

        // Nueva validación de categorías
        Set<CategoryEntity> categories = findActiveCategories(dto.getCategoryIds());

        ProductModel model = ProductMapper.toModelFromDTO(dto);

        ProductEntity entity = new ProductEntity();
        entity.setName(model.getName());
        entity.setPrice(model.getPrice());
        entity.setStock(model.getStock());
        entity.setOwner(owner);
        entity.setCategories(categories);

        return ProductMapper.toResponse(productRepository.save(entity));
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto, UserDetailsImpl currentUser) {
        ProductEntity entity = findEntityOrThrow(id);
        validateOwnership(entity, currentUser);

        // Nueva validación de nombre (excluyendo el actual)
        validateProductNameForUpdate(id, dto.getName());

        // Nueva validación de categorías
        Set<CategoryEntity> categories = findActiveCategories(dto.getCategoryIds());

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setCategories(categories);

        return ProductMapper.toResponse(productRepository.save(entity));
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto, UserDetailsImpl currentUser) {
        ProductEntity entity = findEntityOrThrow(id);
        validateOwnership(entity, currentUser);

        if (dto.getName() != null) {
            validateProductNameForUpdate(id, dto.getName());
            entity.setName(dto.getName());
        }
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getStock() != null) entity.setStock(dto.getStock());

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            entity.setCategories(findActiveCategories(dto.getCategoryIds()));
        }

        return ProductMapper.toResponse(productRepository.save(entity));
    }

    @Override
    public void delete(Long id, UserDetailsImpl currentUser) {
        ProductEntity entity = findEntityOrThrow(id);
        validateOwnership(entity, currentUser);
        entity.setDeleted(true);
        productRepository.save(entity);
    }

    // --- MÉTODOS DE BÚSQUEDA Y FILTROS ---

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByUserId(Long userId) {
        return productRepository.findByOwner_IdAndDeletedFalse(userId).stream()
                .map(ProductMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {
        return productRepository.findByCategories_IdAndDeletedFalse(categoryId).stream()
                .map(ProductMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters) {
        validateFilters(filters);
        return productRepository.findByOwnerIdWithFilters(
                        userId, normalizeName(filters.getName()), filters.getMinPrice(), filters.getMaxPrice(), filters.getCategoryId())
                .stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByUserDto filters) {
        validateFilters(filters);
        return productRepository.findByCategoryIdWithFilters(
                        categoryId, normalizeName(filters.getName()), filters.getMinPrice(), filters.getMaxPrice(), filters.getUserId())
                .stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public Slice<ProductResponseDto> getMyProducts(Long userId, Pageable pageable) {
        // 1. Llamas al repositorio (usando el método que definimos antes)
        Slice<ProductEntity> productSlice = productRepository.findByOwner_IdAndDeletedFalse(userId, pageable);

        // 2. Conviertes de Entity a DTO para evitar el error 500 (referencia circular)
        return productSlice.map(ProductMapper::toResponse);
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private ProductEntity findEntityOrThrow(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    private UserEntity findCurrentUserEntity(UserDetailsImpl currentUser) {
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        return userRepository.findByIdAndDeletedFalse(currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Usuario no autorizado"));
    }

    private void validateOwnership(ProductEntity product, UserDetailsImpl currentUser) {
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        if (hasRole(currentUser, "ROLE_ADMIN")) {
            return;
        }
        if (product.getOwner() == null || product.getOwner().getId() == null) {
            throw new AccessDeniedException("El producto no tiene propietario válido");
        }
        if (!product.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No puedes modificar productos ajenos");
        }
    }

    private boolean hasRole(UserDetailsImpl user, String role) {
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    private Pageable createPageable(PaginationDto pagination) {
        return PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                Sort.by(normalizeDirection(pagination.getDirection()), normalizeSortBy(pagination.getSortBy()))
        );
    }

    private void validateProductNameForCreate(String name) {
        if (productRepository.findByNameIgnoreCaseAndDeletedFalse(name.trim()).isPresent()) {
            throw new ConflictException("Product name already registered");
        }
    }

    private void validateProductNameForUpdate(Long currentProductId, String name) {
        productRepository.findByNameIgnoreCaseAndDeletedFalse(name.trim())
                .filter(product -> !product.getId().equals(currentProductId))
                .ifPresent(product -> {
                    throw new ConflictException("Product name already registered");
                });
    }

    private Set<CategoryEntity> findActiveCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BadRequestException("Debe seleccionar al menos una categoría");
        }
        Set<Long> uniqueIds = new HashSet<>(categoryIds);
        Set<CategoryEntity> categories = categoryRepository.findAllById(uniqueIds)
                .stream()
                .filter(category -> !category.isDeleted())
                .collect(Collectors.toSet());

        if (categories.size() != uniqueIds.size()) {
            throw new NotFoundException("One or more categories were not found");
        }
        return categories;
    }

    private void validateFilters(ProductFilterByUserDto filters) {
        if (filters == null) return;
        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor al precio mínimo");
        }
    }

    private String normalizeName(String name) {
        return (name == null || name.isBlank()) ? null : name.trim();
    }

    private String normalizeSortBy(String sortBy) {
        Set<String> allowedFields = Set.of("id", "name", "price", "stock", "createdAt", "updatedAt");
        return (sortBy != null && allowedFields.contains(sortBy)) ? sortBy : "id";
    }

    private Sort.Direction normalizeDirection(String direction) {
        return (direction != null && direction.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}