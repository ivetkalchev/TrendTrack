package trendtrack.domain.order;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;
import trendtrack.domain.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private User user;
    private List<OrderItem> items;
    private String address;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private double totalAmount;
}