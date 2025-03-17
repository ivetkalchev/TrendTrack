package trendtrack.business.impl;

import lombok.*;
import java.util.List;
import trendtrack.business.*;
import java.time.LocalDateTime;
import trendtrack.persistence.*;
import trendtrack.domain.order.*;
import trendtrack.business.mapper.*;
import trendtrack.business.exception.*;
import trendtrack.business.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import trendtrack.persistence.entity.order.*;
import trendtrack.persistence.OrderRepository;
import org.springframework.stereotype.Service;
import trendtrack.persistence.entity.fabric.*;
import trendtrack.persistence.entity.user.UserEntity;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final FabricRepository fabricRepository;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        if (request == null || request.getItems() == null || request.getUser() == null) {
            throw OrderException.invalidOrder();
        }

        UserEntity userEntity = userMapper.convertToEntity(request.getUser());

        List<OrderItemEntity> orderItems = request.getItems().stream()
                .map(item -> {
                    FabricEntity fabric = fabricRepository.findById(item.getFabric().getId())
                            .orElseThrow(FabricException::fabricDoesNotExist);

                    if (fabric.getStock() < item.getQuantity()) {
                        throw FabricException.insufficientStock();
                    }

                    fabric.setStock(fabric.getStock() - item.getQuantity());
                    fabricRepository.save(fabric);

                    OrderItemEntity orderItemEntity = new OrderItemEntity();
                    orderItemEntity.setFabric(fabric);
                    orderItemEntity.setQuantity(item.getQuantity());
                    orderItemEntity.setPricePerUnit(fabric.getPrice());
                    orderItemEntity.setTotalPrice(item.getTotalPrice());
                    return orderItemEntity;
                })
                .toList();

        OrderEntity orderEntity = OrderEntity.builder()
                .user(userEntity)
                .items(orderItems)
                .address(request.getAddress())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(orderItems.stream()
                        .mapToDouble(OrderItemEntity::getTotalPrice).sum())
                .build();

        for (OrderItemEntity item : orderItems) {
            item.setOrder(orderEntity);
        }

        orderEntity = orderRepository.save(orderEntity);

        cartService.clearCartForUser(request.getUser().getId());

        return orderMapper.convertToResponse(orderEntity);
    }

    @Override
    @Transactional
    public GetAllOrdersResponse getAllOrders(GetAllOrdersRequest request) {
        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<OrderEntity> orderPage = orderRepository.findByFilters(
                request.getId(),
                request.getOrderDate(),
                request.getStatus(),
                pageable
        );

        List<Order> orders = orderPage.getContent().stream()
                .map(orderMapper::convertToDomain)
                .toList();

        return GetAllOrdersResponse.builder()
                .orders(orders)
                .build();
    }

    @Override
    @Transactional
    public CreateOrderResponse updateOrder(UpdateOrderRequest request) {
        OrderEntity orderEntity = orderRepository.findById(request.getId())
                .orElseThrow(OrderException::orderNotFound);

        if (request.getStatus() != null) {
            orderEntity.setStatus(request.getStatus());
        }

        orderEntity = orderRepository.save(orderEntity);

        return orderMapper.convertToResponse(orderEntity);
    }

    @Override
    @Transactional
    public CreateOrderResponse getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(OrderException::orderNotFound);

        return orderMapper.convertToResponse(orderEntity);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw OrderException.orderNotFound();
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GetAllOrdersResponse getOrdersByUserId(Long userId) {
        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
        List<Order> orders = orderEntities.stream()
                .map(orderMapper::convertToDomain)
                .toList();

        return GetAllOrdersResponse.builder()
                .orders(orders)
                .build();
    }
}