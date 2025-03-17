package business;

import java.util.*;
import org.mockito.*;
import org.junit.jupiter.api.*;
import trendtrack.domain.fabric.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import trendtrack.business.mapper.FabricMapper;
import trendtrack.persistence.FabricRepository;
import static org.junit.jupiter.api.Assertions.*;
import trendtrack.business.impl.FabricServiceImpl;
import trendtrack.business.exception.FabricException;
import trendtrack.persistence.entity.fabric.FabricEntity;

class FabricServiceImplTest {

    @InjectMocks
    private FabricServiceImpl fabricService;

    @Mock
    private FabricRepository fabricRepository;

    @Mock
    private FabricMapper fabricMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFabrics_ShouldReturnAllFabrics_WhenRepositoryHasData() {
        //arrange
        FabricEntity fabricEntity = FabricEntity.builder()
                .id(1L)
                .name("Fabric")
                .description("Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(100.0)
                .washable(true)
                .ironed(false)
                .stock(10)
                .pictureUrl("http://example.com/fabric.jpg")
                .build();

        Fabric fabric = new Fabric(
                1L,
                "Fabric",
                "Description",
                Material.COTTON,
                Color.WHITE,
                100.0,
                true,
                false,
                10,
                "http://example.com/fabric.jpg");

        GetAllFabricsRequest request = new GetAllFabricsRequest();
        request.setPage(0);
        request.setSize(10);

        when(fabricRepository.findByFilters(
                request.getName(),
                request.getMaterial(),
                request.getColor(),
                request.getMinPrice(),
                request.getMaxPrice(),
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(List.of(fabricEntity), PageRequest.of(0, 10), 1));

        when(fabricMapper.convertToDomain(fabricEntity)).thenReturn(fabric);

        //act
        GetAllFabricResponse response = fabricService.getFabrics(request);

        //assert
        assertNotNull(response);
        assertEquals(1, response.getFabrics().size());
        Fabric returnedFabric = response.getFabrics().get(0);
        assertEquals("Fabric", returnedFabric.getName());
        assertEquals(Color.WHITE, returnedFabric.getColor());
        assertEquals(Material.COTTON, returnedFabric.getMaterial());
        assertEquals("http://example.com/fabric.jpg", returnedFabric.getPictureUrl());
    }

    @Test
    void getFabrics_ShouldReturnNoFabrics_WhenNoFabricsMatchFilters() {
        //arrange
        GetAllFabricsRequest request = new GetAllFabricsRequest();
        request.setPage(0);
        request.setSize(10);

        when(fabricRepository.findByFilters(
                request.getName(),
                request.getMaterial(),
                request.getColor(),
                request.getMinPrice(),
                request.getMaxPrice(),
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        //act
        GetAllFabricResponse response = fabricService.getFabrics(request);

        //assert
        assertNotNull(response);
        assertEquals(0, response.getFabrics().size());
    }

    @Test
    void getFabrics_ShouldReturnFilteredFabrics_WhenFiltersAreApplied() {
        //arrange
        FabricEntity fabricEntity = FabricEntity.builder()
                .id(1L)
                .name("Fabric")
                .description("Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(100.0)
                .washable(true)
                .ironed(false)
                .stock(10)
                .pictureUrl("http://example.com/fabric.jpg")
                .build();

        Fabric fabric = new Fabric(
                1L,
                "Fabric",
                "Description",
                Material.COTTON,
                Color.WHITE,
                100.0,
                true,
                false,
                10,
                "http://example.com/fabric.jpg");

        GetAllFabricsRequest request = new GetAllFabricsRequest();
        request.setName("Fabric");
        request.setMaterial(Material.COTTON);
        request.setColor(Color.WHITE);
        request.setMinPrice(50.0);
        request.setMaxPrice(150.0);
        request.setPage(0);
        request.setSize(10);

        when(fabricRepository.findByFilters(
                request.getName(),
                request.getMaterial(),
                request.getColor(),
                request.getMinPrice(),
                request.getMaxPrice(),
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(List.of(fabricEntity), PageRequest.of(0, 10), 1));

        when(fabricMapper.convertToDomain(fabricEntity)).thenReturn(fabric);

        //act
        GetAllFabricResponse response = fabricService.getFabrics(request);

        //assert
        assertNotNull(response);
        assertEquals(1, response.getFabrics().size());
        Fabric returnedFabric = response.getFabrics().get(0);
        assertEquals("Fabric", returnedFabric.getName());
        assertEquals(Color.WHITE, returnedFabric.getColor());
        assertEquals(Material.COTTON, returnedFabric.getMaterial());
        assertEquals("http://example.com/fabric.jpg", returnedFabric.getPictureUrl());
    }

    @Test
    void getFabrics_ShouldHandlePagination_WhenMultiplePagesExist() {
        //arrange
        FabricEntity fabricEntity1 = FabricEntity.builder()
                .id(1L)
                .name("Fabric1")
                .description("Description1")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(100.0)
                .washable(true)
                .ironed(false)
                .stock(10)
                .pictureUrl("http://example.com/fabric1.jpg")
                .build();
        FabricEntity fabricEntity2 = FabricEntity.builder()
                .id(2L)
                .name("Fabric2")
                .description("Description2")
                .material(Material.SILK)
                .color(Color.RED)
                .price(120.0)
                .washable(false)
                .ironed(true)
                .stock(5)
                .pictureUrl("http://example.com/fabric2.jpg")
                .build();

        Fabric fabric1 = new Fabric(1L, "Fabric1", "Description1", Material.COTTON, Color.WHITE, 100.0, true, false, 10, "http://example.com/fabric1.jpg");
        Fabric fabric2 = new Fabric(2L, "Fabric2", "Description2", Material.SILK, Color.RED, 120.0, false, true, 5, "http://example.com/fabric2.jpg");

        GetAllFabricsRequest request = new GetAllFabricsRequest();
        request.setPage(0);
        request.setSize(2);

        when(fabricRepository.findByFilters(
                request.getName(),
                request.getMaterial(),
                request.getColor(),
                request.getMinPrice(),
                request.getMaxPrice(),
                PageRequest.of(0, 2)
        )).thenReturn(new PageImpl<>(List.of(fabricEntity1, fabricEntity2), PageRequest.of(0, 2), 2));

        when(fabricMapper.convertToDomain(fabricEntity1)).thenReturn(fabric1);
        when(fabricMapper.convertToDomain(fabricEntity2)).thenReturn(fabric2);

        //act
        GetAllFabricResponse response = fabricService.getFabrics(request);

        //assert
        assertNotNull(response);
        assertEquals(2, response.getFabrics().size());
        assertEquals("Fabric1", response.getFabrics().get(0).getName());
        assertEquals("Fabric2", response.getFabrics().get(1).getName());
    }

    @Test
    void getFabric_ShouldThrowFabricException_WhenFabricDoesNotExist() {
        //arrange
        when(fabricRepository.findById(1L)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(FabricException.class, () -> fabricService.getFabric(1L));
    }

    @Test
    void createFabric_ShouldCreateNewFabric_WhenValidRequest() {
        //arrange
        CreateFabricRequest request = CreateFabricRequest.builder()
                .name("New Fabric")
                .description("Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(150.0)
                .washable(true)
                .ironed(false)
                .stock(20)
                .pictureUrl("http://example.com/fabric.jpg")
                .build();

        when(fabricRepository.existsByName(request.getName())).thenReturn(false);
        when(fabricRepository.save(any(FabricEntity.class))).thenReturn(FabricEntity.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .material(request.getMaterial())
                .color(request.getColor())
                .price(request.getPrice())
                .washable(request.isWashable())
                .ironed(request.isIroned())
                .stock(request.getStock())
                .pictureUrl(request.getPictureUrl())
                .build());

        //act
        CreateFabricResponse response = fabricService.createFabric(request);

        //assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("New Fabric", response.getName());
        assertEquals(Material.COTTON, response.getMaterial());
        assertEquals(Color.WHITE, response.getColor());
        assertEquals("http://example.com/fabric.jpg", response.getPictureUrl());
    }

    @Test
    void createFabric_ShouldThrowFabricException_WhenFabricAlreadyExists() {
        //arrange
        CreateFabricRequest request = CreateFabricRequest.builder()
                .name("Existing Fabric")
                .description("Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(150.0)
                .washable(true)
                .ironed(false)
                .stock(20)
                .pictureUrl("http://example.com/fabric.jpg")
                .build();

        when(fabricRepository.existsByName(request.getName())).thenReturn(true);

        //act n assert
        assertThrows(FabricException.class, () -> fabricService.createFabric(request));
    }

    @Test
    void updateFabric_ShouldUpdateFabric_WhenValidRequest() {
        //arrange
        Long fabricId = 1L;

        UpdateFabricRequest request = UpdateFabricRequest.builder()
                .id(fabricId)
                .name("Updated Fabric")
                .description("Updated Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(200.0)
                .washable(true)
                .ironed(true)
                .stock(50)
                .pictureUrl("http://example.com/fabric.jpg")
                .build();

        FabricEntity existingFabric = FabricEntity.builder()
                .id(fabricId)
                .name("Fabric")
                .description("Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(100.0)
                .washable(true)
                .ironed(false)
                .stock(10)
                .pictureUrl("http://example.com/old_fabric.jpg")
                .build();

        when(fabricRepository.findById(fabricId)).thenReturn(Optional.of(existingFabric));

        //act
        fabricService.updateFabric(request);

        //assert
        verify(fabricRepository, times(1)).save(any(FabricEntity.class));
        assertEquals("Updated Fabric", existingFabric.getName());
        assertEquals("http://example.com/fabric.jpg", existingFabric.getPictureUrl());
    }

    @Test
    void updateFabric_ShouldThrowFabricException_WhenFabricDoesNotExist() {
        //arrange
        Long fabricId = 1L;

        UpdateFabricRequest request = UpdateFabricRequest.builder()
                .id(fabricId)
                .name("Updated Fabric")
                .description("Updated Description")
                .material(Material.COTTON)
                .color(Color.WHITE)
                .price(200.0)
                .washable(true)
                .ironed(true)
                .stock(50)
                .pictureUrl("http://example.com/fabric.jpg")
                .build();

        when(fabricRepository.findById(fabricId)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(FabricException.class, () -> fabricService.updateFabric(request));
    }

    @Test
    void deleteFabric_ShouldDeleteFabric_WhenFabricExists() {
        //arrange
        Long fabricId = 1L;
        when(fabricRepository.existsById(fabricId)).thenReturn(true);

        //act
        fabricService.deleteFabric(fabricId);

        //assert
        verify(fabricRepository, times(1)).deleteById(fabricId);
    }

    @Test
    void deleteFabric_ShouldThrowFabricException_WhenFabricDoesNotExist() {
        //arrange
        Long fabricId = 1L;
        when(fabricRepository.existsById(fabricId)).thenReturn(false);

        //act n assert
        assertThrows(FabricException.class, () -> fabricService.deleteFabric(fabricId));
    }
}