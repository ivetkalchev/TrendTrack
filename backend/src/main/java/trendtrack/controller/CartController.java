package trendtrack.controller;

import lombok.*;
import jakarta.validation.*;
import trendtrack.domain.cart.*;
import org.springframework.http.*;
import jakarta.annotation.security.*;
import trendtrack.business.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @RolesAllowed({"CLIENT"})
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @RolesAllowed({"CLIENT"})
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long userId,
                                              @Valid @RequestBody AddItemRequest request) {
        Cart cart = cartService.addItemToCart(userId, request.getFabricId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @RolesAllowed({"CLIENT"})
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long userId,
                                                   @RequestParam Long fabricId) {
        Cart cart = cartService.removeItemFromCart(userId, fabricId);
        return ResponseEntity.ok(cart);
    }

    @RolesAllowed({"CLIENT"})
    @PutMapping("/{userId}/update")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long userId,
                                               @Valid @RequestBody UpdateCartItemRequest request) {

        Cart cart = cartService.updateCartFromCart(userId, request.getFabricId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }
}