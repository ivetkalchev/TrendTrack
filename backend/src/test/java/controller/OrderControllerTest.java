package controller;

import java.util.List;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;
import trendtrack.domain.order.*;
import trendtrack.domain.fabric.*;
import trendtrack.domain.user.User;
import static org.mockito.Mockito.*;
import trendtrack.domain.order.Order;
import trendtrack.business.OrderService;
import trendtrack.TrendTrackApplication;
import org.springframework.http.MediaType;
import trendtrack.controller.OrderController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import trendtrack.configuration.security.token.AccessTokenDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(OrderController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin", roles = "ADMIN")
@ContextConfiguration(classes = {TrendTrackApplication.class})
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccessTokenDecoder accessTokenDecode;

    // POST /orders
    @Test
    void createOrder_ShouldReturnCreatedOrder_WhenValidRequest() throws Exception {
        //arrange
        CreateOrderResponse mockResponse = new CreateOrderResponse(
                1L,
                new User(),
                List.of(new OrderItem()),
                "address",
                LocalDateTime.now(),
                OrderStatus.PENDING,
                100.0
        );

        Fabric fabric = new Fabric(
                1L,
                "FabricName",
                "FabricDescription",
                Material.COTTON,
                Color.WHITE,
                50.0,
                true,
                true,
                100,
                "url");

        List<OrderItem> items = List.of(new OrderItem(
                1L,
                fabric,
                50,
                50.0,
                100.0));

        CreateOrderRequest request = CreateOrderRequest.builder()
                .user(new User(2L,
                        "client",
                        "password",
                        "client@example.com",
                        "First",
                        "Last"))
                .items(items)
                .address("address")
                .totalAmount(100.0)
                .build();

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\": {\"username\": \"client\", \"email\": \"client@example.com\"}, \"items\": [{\"fabric\": {\"id\": 1}, \"quantity\": 2, \"totalPrice\": 100.0}], \"address\": \"address\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(100.0));

        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    // GET /orders
    @Test
    void getAllOrders_ShouldReturnAllOrders_WhenAdminRequests() throws Exception {
        //arrange
        GetAllOrdersResponse mockResponse = new GetAllOrdersResponse(
                List.of(new Order(
                        1L,
                        new User(),
                        List.of(),
                        "address",
                        LocalDateTime.now(),
                        OrderStatus.PENDING,
                        100.0)));

        when(orderService.getAllOrders(any(GetAllOrdersRequest.class))).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray());

        verify(orderService, times(1)).getAllOrders(any(GetAllOrdersRequest.class));
    }

    // GET /orders with filters
    @Test
    void getAllOrders_ShouldReturnFilteredOrders_WhenFiltersApplied() throws Exception {
        //arrange
        GetAllOrdersResponse mockResponse = new GetAllOrdersResponse(
                List.of(new Order(
                        1L,
                        new User(),
                        List.of(),
                        "address",
                        LocalDateTime.now(),
                        OrderStatus.PENDING,
                        100.0)));

        when(orderService.getAllOrders(any(GetAllOrdersRequest.class))).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "PENDING")
                        .param("orderDate", "2023-01-01T00:00:00")
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].status").value("PENDING"));

        verify(orderService, times(1)).getAllOrders(any(GetAllOrdersRequest.class));
    }

    // GET /orders with invalid parameters
    @Test
    void getAllOrders_ShouldReturnBadRequest_WhenInvalidPagination() throws Exception {
        //act n assert
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "-1")
                        .param("size", "abc"))
                .andExpect(status().isBadRequest());

        verify(orderService, times(0)).getAllOrders(any(GetAllOrdersRequest.class));
    }

    // GET /orders/{id}
    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() throws Exception {
        //arrange
        Long orderId = 1L;
        CreateOrderResponse mockResponse = new CreateOrderResponse(
                1L,
                new User(),
                List.of(new OrderItem()),
                "address",
                LocalDateTime.now(),
                OrderStatus.PENDING,
                100.0
        );

        when(orderService.getOrderById(orderId)).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(100.0));

        verify(orderService, times(1)).getOrderById(orderId);
    }

    // PUT /orders/{id}
    @Test
    void updateOrder_ShouldReturnUpdatedOrder_WhenValidRequest() throws Exception {
        //arrange
        UpdateOrderRequest updateRequest = new UpdateOrderRequest(1L, OrderStatus.SHIPPED);

        User mockUser = new User(
                2L,
                "client",
                "password",
                "client@example.com",
                "First",
                "Last");

        List<OrderItem> mockItems = List.of(new OrderItem(
                1L,
                new Fabric(
                        1L,
                        "FabricName",
                        "FabricDescription",
                        Material.COTTON,
                        Color.WHITE,
                        50.0,
                        true,
                        true,
                        100,
                        "url"),
                2,
                50.0,
                100.0));

        CreateOrderResponse updatedResponse = new CreateOrderResponse(
                1L,
                mockUser,
                mockItems,
                "address",
                LocalDateTime.now(),
                OrderStatus.SHIPPED,
                100.0
        );

        when(orderService.updateOrder(updateRequest)).thenReturn(updatedResponse);

        //act n assert
        mockMvc.perform(put("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"status\": \"SHIPPED\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));

        verify(orderService, times(1)).updateOrder(any(UpdateOrderRequest.class));
    }

    // DELETE /orders/{id}
    @Test
    void deleteOrder_ShouldReturnNoContent_WhenOrderExists() throws Exception {
        //arrange
        Long orderId = 1L;
        doNothing().when(orderService).deleteOrder(orderId);

        //act n assert
        mockMvc.perform(delete("/orders/{id}", orderId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(orderId);
    }
}