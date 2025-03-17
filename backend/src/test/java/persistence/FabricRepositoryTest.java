package persistence;

import org.mockito.*;
import java.util.List;
import org.junit.jupiter.api.*;
import trendtrack.domain.fabric.*;
import static org.mockito.Mockito.*;
import trendtrack.TrendTrackApplication;
import org.springframework.data.domain.*;
import trendtrack.persistence.FabricRepository;
import static org.assertj.core.api.Assertions.assertThat;
import trendtrack.persistence.entity.fabric.FabricEntity;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TrendTrackApplication.class)
public class FabricRepositoryTest {

    @Mock
    private FabricRepository fabricRepository;

    @Mock
    private Pageable pageable;

    private FabricEntity cottonFabric;

    @BeforeEach
    void setUp() {
        cottonFabric = new FabricEntity();
        cottonFabric.setName("Cotton");
        cottonFabric.setDescription("A high-quality cotton fabric.");
        cottonFabric.setColor(Color.RED);
        cottonFabric.setMaterial(Material.COTTON);
        cottonFabric.setPrice(50.0);
        cottonFabric.setWashable(true);
        cottonFabric.setIroned(true);
        cottonFabric.setStock(100);
    }

    @Test
    void testFindByFilters_ShouldReturnCottonFabric_WhenNameMatches() {
        //arrange
        String nameFilter = "Cotton";
        when(fabricRepository.findByFilters(eq(nameFilter), isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric)));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(nameFilter, null, null, null, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Cotton");
        verify(fabricRepository, times(1)).findByFilters(eq(nameFilter), isNull(), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnCottonFabric_WhenMaterialMatches() {
        //arrange
        Material materialFilter = Material.COTTON;
        when(fabricRepository.findByFilters(isNull(), eq(materialFilter), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric)));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(null, materialFilter, null, null, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getMaterial()).isEqualTo(Material.COTTON);
        verify(fabricRepository, times(1)).findByFilters(isNull(), eq(materialFilter), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnCottonFabric_WhenColorMatches() {
        //arrange
        Color colorFilter = Color.RED;
        when(fabricRepository.findByFilters(isNull(), isNull(), eq(colorFilter), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric)));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(null, null, colorFilter, null, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getColor()).isEqualTo(Color.RED);
        verify(fabricRepository, times(1)).findByFilters(isNull(), isNull(), eq(colorFilter), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnCottonFabric_WhenMinPriceMatches() {
        //arrange
        Double minPriceFilter = 40.0;
        when(fabricRepository.findByFilters(isNull(), isNull(), isNull(), eq(minPriceFilter), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric)));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(null, null, null, minPriceFilter, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrice()).isGreaterThanOrEqualTo(minPriceFilter);
        verify(fabricRepository, times(1)).findByFilters(isNull(), isNull(), isNull(), eq(minPriceFilter), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnCottonFabric_WhenMaxPriceMatches() {
        //arrange
        Double maxPriceFilter = 60.0;
        when(fabricRepository.findByFilters(isNull(), isNull(), isNull(), isNull(), eq(maxPriceFilter), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric)));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(null, null, null, null, maxPriceFilter, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrice()).isLessThanOrEqualTo(maxPriceFilter);
        verify(fabricRepository, times(1)).findByFilters(isNull(), isNull(), isNull(), isNull(), eq(maxPriceFilter), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnCottonFabric_WhenNameAndMaterialMatch() {
        //arrange
        String nameFilter = "Cotton";
        Material materialFilter = Material.COTTON;
        when(fabricRepository.findByFilters(eq(nameFilter), eq(materialFilter), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric)));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(nameFilter, materialFilter, null, null, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Cotton");
        assertThat(result.getContent().get(0).getMaterial()).isEqualTo(Material.COTTON);
        verify(fabricRepository, times(1)).findByFilters(eq(nameFilter), eq(materialFilter), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnPagedResults() {
        //arrange
        when(fabricRepository.findByFilters(
                isNull(), isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(cottonFabric, cottonFabric), PageRequest.of(0, 2), 2));

        //act
        Page<FabricEntity> result = fabricRepository.findByFilters(
                null, null, null, null, null, PageRequest.of(0, 2));

        //assert
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        verify(fabricRepository, times(1)).findByFilters(
                isNull(), isNull(), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void testExistsByNameTrue() {
        //arrange
        when(fabricRepository.existsByName("Cotton")).thenReturn(true);

        //act
        boolean exists = fabricRepository.existsByName("Cotton");

        //assert
        assertThat(exists).isTrue();

        verify(fabricRepository, times(1)).existsByName("Cotton");
    }

    @Test
    void testExistsByNameFalse() {
        //arrange
        when(fabricRepository.existsByName("Silk")).thenReturn(false);

        //act
        boolean exists = fabricRepository.existsByName("Silk");

        //assert
        assertThat(exists).isFalse();

        verify(fabricRepository, times(1)).existsByName("Silk");
    }
}