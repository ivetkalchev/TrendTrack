package persistence;

import org.mockito.*;
import java.util.List;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import trendtrack.TrendTrackApplication;
import org.springframework.data.domain.*;
import trendtrack.domain.order.OrderStatus;
import trendtrack.persistence.OrderRepository;
import org.springframework.data.domain.Pageable;
import trendtrack.persistence.entity.user.UserEntity;
import trendtrack.persistence.entity.order.OrderEntity;
import trendtrack.persistence.entity.fabric.FabricEntity;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.mock.mockito.MockBean;
import trendtrack.persistence.entity.order.OrderItemEntity;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TrendTrackApplication.class)
public class OrderRepositoryTest {

    @MockBean
    private OrderRepository orderRepository;

    private UserEntity user;
    private OrderEntity order1;
    private OrderEntity order2;

    @Mock
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");

        FabricEntity fabric = new FabricEntity();
        fabric.setName("Cotton");
        fabric.setDescription("A high-quality cotton fabric.");
        fabric.setColor(trendtrack.domain.fabric.Color.RED);
        fabric.setMaterial(trendtrack.domain.fabric.Material.COTTON);
        fabric.setPrice(50.0);
        fabric.setWashable(true);
        fabric.setIroned(true);
        fabric.setStock(100);

        OrderItemEntity orderItem1 = new OrderItemEntity();
        orderItem1.setFabric(fabric);
        orderItem1.setQuantity(2);
        orderItem1.setPricePerUnit(50.0);
        orderItem1.setTotalPrice(100.0);

        OrderItemEntity orderItem2 = new OrderItemEntity();
        orderItem2.setFabric(fabric);
        orderItem2.setQuantity(1);
        orderItem2.setPricePerUnit(50.0);
        orderItem2.setTotalPrice(50.0);

        order1 = OrderEntity.builder()
                .user(user)
                .address("123 Main St")
                .orderDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                .status(OrderStatus.PENDING)
                .totalAmount(150.0)
                .items(List.of(orderItem1, orderItem2))
                .build();

        order2 = OrderEntity.builder()
                .user(user)
                .address("456 Oak Ave")
                .orderDate(LocalDateTime.of(2025, 1, 2, 14, 0))
                .status(OrderStatus.DELIVERED)
                .totalAmount(50.0)
                .items(List.of(orderItem2))
                .build();
    }

    @Test
    void testFindByFilters_ShouldReturnOrders_WhenStatusMatches() {
        //arrange
        OrderStatus statusFilter = OrderStatus.PENDING;
        when(orderRepository.findByFilters(isNull(), isNull(), eq(statusFilter), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order1)));

        //act
        Page<OrderEntity> result = orderRepository.findByFilters(null, null, statusFilter, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository, times(1))
                .findByFilters(isNull(), isNull(), eq(statusFilter), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnOrders_WhenOrderDateMatches() {
        //arrange
        LocalDateTime dateFilter = LocalDateTime
                .of(2025, 1, 1, 10, 0, 0, 0);
        when(orderRepository.findByFilters(isNull(), eq(dateFilter), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order1)));

        //act
        Page<OrderEntity> result = orderRepository.findByFilters(null, dateFilter, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getOrderDate()).isEqualTo(dateFilter);
        verify(orderRepository, times(1))
                .findByFilters(isNull(), eq(dateFilter), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnOrders_WhenIdMatches() {
        //arrange
        Long idFilter = 1L;
        OrderEntity mockOrder = new OrderEntity();
        mockOrder.setId(idFilter);

        when(orderRepository.findByFilters(eq(idFilter), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockOrder)));

        //act
        Page<OrderEntity> result = orderRepository.findByFilters(idFilter, null, null, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(idFilter);
        verify(orderRepository, times(1))
                .findByFilters(eq(idFilter), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnOrders_WhenStatusAndDateMatch() {
        //arrange
        OrderStatus statusFilter = OrderStatus.PENDING;
        LocalDateTime dateFilter = LocalDateTime
                .of(2025, 1, 1, 0, 0, 0, 0);
        OrderEntity mockOrder = new OrderEntity();
        mockOrder.setId(1L);
        mockOrder.setStatus(statusFilter);
        mockOrder.setOrderDate(dateFilter);

        when(orderRepository.findByFilters(eq(1L), eq(dateFilter), eq(statusFilter), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockOrder)));

        //act
        Page<OrderEntity> result = orderRepository.findByFilters(1L, dateFilter, statusFilter, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getContent().get(0).getOrderDate()).isEqualTo(dateFilter);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);

        verify(orderRepository, times(1)).findByFilters(eq(1L),
                eq(dateFilter), eq(statusFilter), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldHandlePagination() {
        //arrange
        when(orderRepository.findByFilters(isNull(), isNull(), isNull(), eq(PageRequest.of(0, 1))))
                .thenReturn(new PageImpl<>(List.of(order1), PageRequest.of(0, 1), 2));

        //act
        Page<OrderEntity> result = orderRepository
                .findByFilters(null, null, null, PageRequest.of(0, 1));

        //assert
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(1);
        verify(orderRepository, times(1))
                .findByFilters(isNull(), isNull(), isNull(), eq(PageRequest.of(0, 1)));
    }

    @Test
    void testFindByUserId() {
        //arrange
        when(orderRepository.findByUserId(user.getId())).thenReturn(List.of(order1, order2));

        //act
        List<OrderEntity> foundOrders = orderRepository.findByUserId(user.getId());

        //assert
        assertThat(foundOrders).isNotNull();
        assertThat(foundOrders).hasSize(2);
        assertThat(foundOrders).contains(order1, order2);

        verify(orderRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void testFindByUserId_NoOrders() {
        //arrange
        when(orderRepository.findByUserId(user.getId())).thenReturn(List.of());

        //act
        List<OrderEntity> foundOrders = orderRepository.findByUserId(user.getId());

        //assert
        assertThat(foundOrders).isEmpty();

        verify(orderRepository, times(1)).findByUserId(user.getId());
    }
}