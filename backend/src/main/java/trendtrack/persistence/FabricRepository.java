package trendtrack.persistence;

import trendtrack.domain.fabric.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import trendtrack.persistence.entity.fabric.FabricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FabricRepository extends JpaRepository<FabricEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT f FROM FabricEntity f WHERE (:name IS NULL OR f.name LIKE %:name%)" +
            " AND (:material IS NULL OR f.material = :material)" +
            " AND (:color IS NULL OR f.color = :color)" +
            " AND (:minPrice IS NULL OR f.price >= :minPrice)" +
            " AND (:maxPrice IS NULL OR f.price <= :maxPrice)")
    Page<FabricEntity> findByFilters(String name, Material material, Color color,
                                     Double minPrice, Double maxPrice,
                                     Pageable pageable);
}