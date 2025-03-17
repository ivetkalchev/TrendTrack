package business;

import java.util.*;
import org.mockito.*;
import org.junit.jupiter.api.*;
import trendtrack.persistence.*;
import trendtrack.domain.order.*;
import trendtrack.domain.user.User;
import static org.mockito.Mockito.*;
import trendtrack.business.mapper.*;
import trendtrack.domain.order.Order;
import trendtrack.domain.fabric.Fabric;
import trendtrack.business.CartService;
import trendtrack.business.exception.*;
import org.springframework.data.domain.*;
import trendtrack.persistence.entity.order.*;
import trendtrack.persistence.entity.fabric.*;
import static org.junit.jupiter.api.Assertions.*;
import trendtrack.business.impl.OrderServiceImpl;
import trendtrack.persistence.entity.user.UserEntity;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartService cartService;

    @Mock
    private FabricRepository fabricRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnOrderResponse_WhenValidRequest() {
        //arrange
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUser(new User());
        request.setAddress("Test Address");

        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setPrice(10.0);
        fabricEntity.setStock(10);
        fabricEntity.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setFabric(new Fabric());
        orderItem.setQuantity(2);
        orderItem.setTotalPrice(20.0);

        request.setItems(List.of(orderItem));

        UserEntity userEntity = new UserEntity();
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setFabric(fabricEntity);
        orderItemEntity.setQuantity(2);
        orderItemEntity.setTotalPrice(20.0);

        when(userMapper.convertToEntity(request.getUser())).thenReturn(userEntity);
        when(fabricRepository.findById(any())).thenReturn(Optional.of(fabricEntity));
        when(orderMapper.convertToResponse(any())).thenReturn(new CreateOrderResponse());
        when(orderRepository.save(any())).thenReturn(new OrderEntity());

        //act
        CreateOrderResponse response = orderService.createOrder(request);

        //assert
        assertNotNull(response);
        verify(orderRepository, times(1)).save(any());
        verify(cartService, times(1)).clearCartForUser(any());
    }

    @Test
    void getAllOrders_ShouldReturnOrdersList_WhenOrdersExist() {
        //arrange
        List<OrderEntity> orderEntities = List.of(new OrderEntity());
        when(orderRepository.findByFilters(any(), any(), any(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(orderEntities));

        when(orderMapper.convertToDomain(any())).thenReturn(new Order());

        GetAllOrdersRequest request = new GetAllOrdersRequest();
        request.setPage(0);
        request.setSize(10);

        //act
        GetAllOrdersResponse response = orderService.getAllOrders(request);

        //assert
        assertNotNull(response);
        assertEquals(1, response.getOrders().size());
        verify(orderRepository, times(1)).findByFilters(any(), any(), any(), any(PageRequest.class));
    }

    @Test
    void getAllOrders_ShouldReturnNoOrders_WhenNoOrdersMatchFilters() {
        //arrange
        when(orderRepository.findByFilters(any(), any(), any(), any(PageRequest.class)))
                .thenReturn(Page.empty());

        GetAllOrdersRequest request = new GetAllOrdersRequest();
        request.setPage(0);
        request.setSize(10);

        //act
        GetAllOrdersResponse response = orderService.getAllOrders(request);

        //assert
        assertNotNull(response);
        assertEquals(0, response.getOrders().size());
    }

    @Test
    void getOrdersByUserId_ShouldReturnOrders_WhenValidUserId() {
        //arrange
        List<OrderEntity> orderEntities = List.of(new OrderEntity());
        when(orderRepository.findByUserId(anyLong())).thenReturn(orderEntities);
        when(orderMapper.convertToDomain(any())).thenReturn(new Order());

        //act
        GetAllOrdersResponse response = orderService.getOrdersByUserId(1L);

        //assert
        assertNotNull(response);
        assertEquals(1, response.getOrders().size());
        verify(orderRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void getOrdersByUserId_ShouldReturnNoOrders_WhenNoOrdersForUser() {
        //arrange
        when(orderRepository.findByUserId(anyLong())).thenReturn(Collections.emptyList());

        //act
        GetAllOrdersResponse response = orderService.getOrdersByUserId(1L);

        //assert
        assertNotNull(response);
        assertEquals(0, response.getOrders().size());
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenValidId() {
        //arrange
        OrderEntity orderEntity = new OrderEntity();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderMapper.convertToResponse(orderEntity)).thenReturn(new CreateOrderResponse());

        //act
        CreateOrderResponse response = orderService.getOrderById(1L);

        //assert
        assertNotNull(response);
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateOrder_ShouldUpdateOrder_WhenValidRequest() {
        //arrange
        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setId(1L);
        request.setStatus(OrderStatus.SHIPPED);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any())).thenReturn(orderEntity);
        when(orderMapper.convertToResponse(orderEntity)).thenReturn(new CreateOrderResponse());

        //act
        CreateOrderResponse response = orderService.updateOrder(request);

        //assert
        assertNotNull(response);
        assertEquals(OrderStatus.SHIPPED, orderEntity.getStatus());
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenValidId() {
        //arrange
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        //act
        orderService.deleteOrder(1L);

        //assert
        verify(orderRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenOrderNotFound() {
        //arrange
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        //act n assert
        assertThrows(OrderException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void createOrder_ShouldThrowException_WhenInvalidOrderRequest() {
        //arrange
        CreateOrderRequest request = new CreateOrderRequest();

        //act n assert
        assertThrows(OrderException.class, () -> orderService.createOrder(request));
    }
}