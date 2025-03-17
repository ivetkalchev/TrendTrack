package trendtrack.domain.cart;

import lombok.*;
import java.util.List;
import trendtrack.domain.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;
    private User user;
    private List<CartItem> items;
    private double totalCost;
}