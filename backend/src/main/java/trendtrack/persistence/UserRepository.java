package trendtrack.persistence;

import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import trendtrack.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    void deleteById(Long id);

    boolean existsById(Long id);

    @Query("SELECT u FROM UserEntity u WHERE (:username IS NULL OR u.username LIKE %:username%)" +
            " AND (:firstName IS NULL OR u.firstName LIKE %:firstName%)" +
            " AND (:lastName IS NULL OR u.lastName LIKE %:lastName%)")
    Page<UserEntity> findByFilters(String username, String firstName, String lastName, Pageable pageable);
}