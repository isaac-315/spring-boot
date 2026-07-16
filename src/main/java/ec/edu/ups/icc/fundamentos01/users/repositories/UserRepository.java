package ec.edu.ups.icc.fundamentos01.users.repositories;

import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Opcional pero recomendado
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByIdAndDeletedFalse(Long id);

    Optional<UserEntity> findByIdAndDeleted(Long id, boolean delete);

    boolean existsByIdAndDeletedFalse(Long id);


}
