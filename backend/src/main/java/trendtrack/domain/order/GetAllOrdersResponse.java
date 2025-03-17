package trendtrack.domain.order;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOrdersResponse {
    private List<Order> orders;
}