package trendtrack.persistence;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.*;
import trendtrack.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import trendtrack.persistence.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserId(Long userId);

    @Query("SELECT o FROM OrderEntity o WHERE " +
            "(:id IS NULL OR o.id = :id) " +
            "AND (:orderDate IS NULL OR o.orderDate >= :orderDate) " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<OrderEntity> findByFilters(Long id, LocalDateTime orderDate,
                                    OrderStatus status, Pageable pageable);
}