package trendtrack.domain.cart;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequest {
    
    @NotNull
    private Long fabricId;

    @Min(1)
    @NotNull
    private int quantity;
}