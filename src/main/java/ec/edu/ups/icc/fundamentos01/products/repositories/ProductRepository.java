package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByNameIgnoreCaseAndDeletedFalse(String name);

    List<ProductEntity> findByDeletedFalse();

    Page<ProductEntity> findByDeletedFalse(Pageable pageable);

    Optional<ProductEntity> findByIdAndDeletedFalse(Long id);

    List<ProductEntity> findByOwner_IdAndDeletedFalse(Long ownerId);

    // CORREGIDO: De vuelta al plural porque la entidad usa Set<CategoryEntity> categories
    List<ProductEntity> findByCategories_IdAndDeletedFalse(Long categoryId);

    // Agrega esta línea en tu ProductRepository
    Slice<ProductEntity> findByOwner_IdAndDeletedFalse(Long ownerId, Pageable pageable);

    @Query("""
            SELECT DISTINCT p
            FROM ProductEntity p
            LEFT JOIN p.categories c
            WHERE p.deleted = false
              AND p.owner.id = :userId
              AND p.owner.deleted = false
              AND (COALESCE(:name, '') = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:name, ''), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:categoryId IS NULL OR (c.id = :categoryId AND c.deleted = false))
            """)
    List<ProductEntity> findByOwnerIdWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categoryId") Long categoryId
    );

    @Query("""
            SELECT DISTINCT p
            FROM ProductEntity p
            JOIN p.categories c
            WHERE p.deleted = false
              AND c.id = :categoryId
              AND c.deleted = false
              AND p.owner.deleted = false
              AND (COALESCE(:name, '') = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:name, ''), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:userId IS NULL OR p.owner.id = :userId)
            """)
    List<ProductEntity> findByCategoryIdWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("userId") Long userId
    );

    @Query(
            value = """
                    SELECT p
                    FROM ProductEntity p
                    WHERE p.deleted = false
                    """,
            countQuery = """
                    SELECT COUNT(p)
                    FROM ProductEntity p
                    WHERE p.deleted = false
                    """
    )
    Page<ProductEntity> findActivePage(Pageable pageable);

    /*
     * Consulta productos activos usando Slice.
     *
     * Slice no necesita total de registros.
     */
    @Query("""
            SELECT p
            FROM ProductEntity p
            WHERE p.deleted = false
            """)
    Slice<ProductEntity> findActiveSlice(Pageable pageable);
}