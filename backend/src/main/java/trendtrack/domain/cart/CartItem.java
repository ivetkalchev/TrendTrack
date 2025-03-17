package trendtrack.domain.cart;

import lombok.*;
import trendtrack.domain.fabric.Fabric;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long id;
    private Fabric fabric;
    private int quantity;
    private double totalPrice;
}