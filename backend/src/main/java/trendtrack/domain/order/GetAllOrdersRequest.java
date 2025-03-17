package trendtrack.domain.order;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOrdersRequest {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private int page;
    private int size;
}