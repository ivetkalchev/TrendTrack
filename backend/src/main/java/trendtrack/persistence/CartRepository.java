package trendtrack.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import trendtrack.persistence.entity.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity findByUser_Id(Long userId);

    @Query("SELECT c FROM CartEntity c JOIN c.items ci WHERE ci.fabric.id = :fabricId")
    List<CartEntity> findByFabricId(Long fabricId);
}