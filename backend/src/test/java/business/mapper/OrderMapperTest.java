package business.mapper;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import trendtrack.domain.order.*;
import org.mapstruct.factory.Mappers;
import trendtrack.domain.order.Order;
import trendtrack.business.mapper.OrderMapper;
import trendtrack.persistence.entity.order.OrderEntity;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = Mappers.getMapper(OrderMapper.class);
    }

    @Test
    void testConvertToDomain() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setAddress("123 Main St");
        orderEntity.setOrderDate(LocalDateTime.of(2025, 1, 1, 10, 0));
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setTotalAmount(200.0);

        Order order = orderMapper.convertToDomain(orderEntity);

        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(orderEntity.getId());
        assertThat(order.getAddress()).isEqualTo(orderEntity.getAddress());
        assertThat(order.getOrderDate()).isEqualTo(orderEntity.getOrderDate());
        assertThat(order.getStatus()).isEqualTo(orderEntity.getStatus());
        assertThat(order.getTotalAmount()).isEqualTo(orderEntity.getTotalAmount());
    }

    @Test
    void testConvertToDomain_Null() {
        OrderEntity orderEntity = null;

        Order order = orderMapper.convertToDomain(orderEntity);

        assertThat(order).isNull();
    }

    @Test
    void testConvertToResponse() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setAddress("123 Main St");
        orderEntity.setOrderDate(LocalDateTime.of(2025, 1, 1, 10, 0));
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setTotalAmount(200.0);

        CreateOrderResponse response = orderMapper.convertToResponse(orderEntity);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(orderEntity.getId());
        assertThat(response.getAddress()).isEqualTo(orderEntity.getAddress());
        assertThat(response.getOrderDate()).isEqualTo(orderEntity.getOrderDate());
        assertThat(response.getStatus()).isEqualTo(orderEntity.getStatus());
        assertThat(response.getTotalAmount()).isEqualTo(orderEntity.getTotalAmount());
    }

    @Test
    void testConvertToResponse_Null() {
        OrderEntity orderEntity = null;

        CreateOrderResponse response = orderMapper.convertToResponse(orderEntity);

        assertThat(response).isNull();
    }
}