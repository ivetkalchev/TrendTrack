package trendtrack.persistence.entity.order;

import lombok.*;
import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import trendtrack.domain.order.OrderStatus;
import trendtrack.persistence.entity.user.UserEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "date")
    private LocalDateTime orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @NotNull
    @Column(name = "total_amount")
    private double totalAmount;
}