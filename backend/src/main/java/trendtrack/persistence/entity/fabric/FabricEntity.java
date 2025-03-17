package trendtrack.persistence.entity.fabric;

import lombok.*;
import jakarta.persistence.*;
import trendtrack.domain.fabric.Color;
import jakarta.validation.constraints.*;
import trendtrack.domain.fabric.Material;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fabric")
public class FabricEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "name")
    @Length(min = 2, max = 100)
    private String name;

    @NotBlank
    @Column(name = "description")
    @Length(min = 2, max = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "material")
    private Material material;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @NotNull
    @Positive
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "washable")
    private boolean washable;

    @NotNull
    @Column(name = "ironed")
    private boolean ironed;

    @NotNull
    @PositiveOrZero
    @Column(name = "stock")
    private int stock;

    @Column(name = "picture_url")
    @Pattern(regexp = "^(https?://.*\\.(png|jpg|jpeg|svg|gif))?$")
    private String pictureUrl;
}