package trendtrack.domain.order;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;
import trendtrack.domain.user.User;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotBlank
    private User user;

    @NotBlank
    private List<OrderItem> items;

    @NotBlank
    private String address;

    @NotNull
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();

    @NotBlank
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @NotBlank
    private double totalAmount;
}