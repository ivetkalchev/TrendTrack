package trendtrack.business;

import trendtrack.domain.order.*;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequest request);

    GetAllOrdersResponse getAllOrders(GetAllOrdersRequest request);

    CreateOrderResponse getOrderById(Long id);

    CreateOrderResponse updateOrder(UpdateOrderRequest request);

    void deleteOrder(Long id);

    GetAllOrdersResponse getOrdersByUserId(Long userId);
}