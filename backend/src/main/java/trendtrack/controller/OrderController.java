package trendtrack.controller;

import lombok.*;
import java.time.LocalDateTime;
import trendtrack.domain.order.*;
import jakarta.annotation.security.*;
import trendtrack.business.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @RolesAllowed({"CLIENT"})
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<GetAllOrdersResponse> getAllOrders(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "orderDate", required = false) LocalDateTime orderDate,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        GetAllOrdersRequest request = GetAllOrdersRequest.builder()
                .id(id)
                .orderDate(orderDate)
                .status(status)
                .page(page)
                .size(size)
                .build();

        GetAllOrdersResponse response = orderService.getAllOrders(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CreateOrderResponse> getOrderById(@PathVariable Long id) {
        CreateOrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CreateOrderResponse> updateOrder(@RequestBody UpdateOrderRequest request) {
        CreateOrderResponse response = orderService.updateOrder(request);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"CLIENT"})
    @GetMapping("/users/{userId}")
    public ResponseEntity<GetAllOrdersResponse> getOrdersByUserId(@PathVariable Long userId) {
        GetAllOrdersResponse response = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(response);
    }
}