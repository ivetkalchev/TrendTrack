package trendtrack.domain.order;

import lombok.*;
import trendtrack.domain.fabric.Fabric;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Long id;
    private Fabric fabric;
    private int quantity;
    private double pricePerUnit;
    private double totalPrice;
}