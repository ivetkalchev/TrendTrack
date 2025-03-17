package trendtrack.domain.fabric;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFabricRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Material material;

    @NotNull
    private Color color;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    private double price;

    @NotNull
    private boolean washable;

    @NotNull
    private boolean ironed;

    @NotNull
    @PositiveOrZero
    private int stock;

    @Pattern(regexp = "^(https?://.*\\.(png|jpg|jpeg|svg|gif))?$")
    private String pictureUrl;
}