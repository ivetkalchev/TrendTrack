package trendtrack.domain.order;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderRequest {

    @NotNull
    private Long id;

    @NotBlank
    private OrderStatus status;
}