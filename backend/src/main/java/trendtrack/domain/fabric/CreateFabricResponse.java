package trendtrack.domain.fabric;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFabricResponse {
    private Long id;
    private String name;
    private String description;
    private Material material;
    private Color color;
    private double price;
    private boolean washable;
    private boolean ironed;
    private int stock;
    private String pictureUrl;
}