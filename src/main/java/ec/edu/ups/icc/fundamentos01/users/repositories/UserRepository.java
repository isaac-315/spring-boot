package ec.edu.ups.icc.fundamentos01.users.repositories;

import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Opcional pero recomendado
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 1. Para las validaciones de "existe por ID y no eliminado"
    boolean existsByIdAndDeletedFalse(Long id);

    // 2. Para buscar usuario por email (usado en login y registro)
    Optional<UserEntity> findByEmail(String email);

    // 3. Para buscar por ID y verificar que no esté eliminado
    Optional<UserEntity> findByIdAndDeletedFalse(Long id);

    // Verifica también si tienes este método, suele ser necesario en registro
    boolean existsByEmail(String email);


}
