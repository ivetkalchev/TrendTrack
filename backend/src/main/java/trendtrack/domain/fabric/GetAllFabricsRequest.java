package trendtrack.domain.fabric;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllFabricsRequest {
    private String name;
    private Material material;
    private Color color;
    private Double minPrice;
    private Double maxPrice;
    private int page;
    private int size;
}