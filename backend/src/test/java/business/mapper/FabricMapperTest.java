package business.mapper;

import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;
import trendtrack.domain.fabric.Fabric;
import trendtrack.business.mapper.FabricMapper;
import trendtrack.persistence.entity.fabric.FabricEntity;
import static org.assertj.core.api.Assertions.assertThat;

public class FabricMapperTest {
    private FabricMapper fabricMapper;

    @BeforeEach
    void setUp() {
        fabricMapper = Mappers.getMapper(FabricMapper.class);
    }

    @Test
    void testConvertToDomain() {
        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setId(1L);
        fabricEntity.setName("Cotton");
        fabricEntity.setDescription("A high-quality cotton fabric.");
        fabricEntity.setColor(trendtrack.domain.fabric.Color.RED);
        fabricEntity.setMaterial(trendtrack.domain.fabric.Material.COTTON);
        fabricEntity.setPrice(50.0);
        fabricEntity.setWashable(true);
        fabricEntity.setIroned(true);
        fabricEntity.setStock(100);

        Fabric fabric = fabricMapper.convertToDomain(fabricEntity);

        assertThat(fabric).isNotNull();
        assertThat(fabric.getId()).isEqualTo(fabricEntity.getId());
        assertThat(fabric.getName()).isEqualTo(fabricEntity.getName());
        assertThat(fabric.getDescription()).isEqualTo(fabricEntity.getDescription());
        assertThat(fabric.getColor()).isEqualTo(fabricEntity.getColor());
        assertThat(fabric.getMaterial()).isEqualTo(fabricEntity.getMaterial());
        assertThat(fabric.getPrice()).isEqualTo(fabricEntity.getPrice());
        assertThat(fabric.isWashable()).isEqualTo(fabricEntity.isWashable());
        assertThat(fabric.isIroned()).isEqualTo(fabricEntity.isIroned());
        assertThat(fabric.getStock()).isEqualTo(fabricEntity.getStock());
    }

    @Test
    void testConvertToDomainNull() {
        FabricEntity fabricEntity = null;

        Fabric fabric = fabricMapper.convertToDomain(fabricEntity);

        assertThat(fabric).isNull();
    }
}