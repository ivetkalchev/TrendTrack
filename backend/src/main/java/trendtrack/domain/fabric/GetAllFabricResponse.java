package trendtrack.domain.fabric;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllFabricResponse {
    private List<Fabric> fabrics;
}