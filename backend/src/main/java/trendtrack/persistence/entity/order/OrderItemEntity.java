package trendtrack.persistence.entity.order;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import trendtrack.persistence.entity.fabric.FabricEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderitem")
public class OrderItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fabric_id")
    private FabricEntity fabric;

    @NotNull
    @Column(name = "quantity")
    private int quantity;

    @NotNull
    @Column(name = "price_per_unit")
    private double pricePerUnit;

    @NotNull
    @Column(name = "total_price")
    private double totalPrice;
}
