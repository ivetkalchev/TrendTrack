package trendtrack.business;

import trendtrack.domain.cart.Cart;
import trendtrack.persistence.entity.fabric.FabricEntity;

public interface CartService {

    Cart getCartByUserId(Long userId);

    Cart addItemToCart(Long userId, Long fabricId, int quantity);

    Cart removeItemFromCart(Long userId, Long fabricId);

    Cart updateCartFromCart(Long userId, Long fabricId, int quantity);

    void clearCartForUser(Long userId);

    void notifyClientsOfLowStock(FabricEntity fabric);
}