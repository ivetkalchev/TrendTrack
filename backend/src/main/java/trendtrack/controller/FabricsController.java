package trendtrack.controller;

import lombok.*;
import jakarta.validation.*;
import trendtrack.domain.fabric.*;
import jakarta.annotation.security.*;
import trendtrack.business.FabricService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/fabrics")
public class FabricsController {
    private final FabricService fabricService;

    @GetMapping("{id}")
    public ResponseEntity<Fabric> getFabric(@PathVariable final Long id) {
        Fabric fabric = fabricService.getFabric(id);
        return ResponseEntity.ok().body(fabric);
    }

    @GetMapping
    public ResponseEntity<GetAllFabricResponse> getAllFabrics(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "material", required = false) Material material,
            @RequestParam(value = "color", required = false) Color color,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        GetAllFabricsRequest request = GetAllFabricsRequest.builder()
                .name(name)
                .material(material)
                .color(color)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .page(page)
                .size(size)
                .build();

        GetAllFabricResponse response = fabricService.getFabrics(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CreateFabricResponse> createFabric(@RequestBody @Valid CreateFabricRequest request) {
        CreateFabricResponse response = fabricService.createFabric(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> deleteFabric(@PathVariable Long id) {
        fabricService.deleteFabric(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> updateFabric(@PathVariable Long id,
                                             @RequestBody @Valid UpdateFabricRequest request) {
        request.setId(id);
        fabricService.updateFabric(request);
        return ResponseEntity.noContent().build();
    }
}