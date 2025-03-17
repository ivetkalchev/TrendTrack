package trendtrack.business.impl;

import lombok.*;
import java.util.*;
import trendtrack.business.*;
import jakarta.transaction.*;
import trendtrack.domain.cart.*;
import trendtrack.persistence.*;
import trendtrack.business.exception.*;
import org.springframework.stereotype.*;
import trendtrack.persistence.entity.user.*;
import trendtrack.persistence.entity.cart.*;
import trendtrack.business.mapper.CartMapper;
import trendtrack.persistence.entity.fabric.*;
import trendtrack.domain.websocket.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final FabricRepository fabricRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @Override
    public Cart getCartByUserId(Long userId) {
        CartEntity cart = cartRepository.findByUser_Id(userId);
        if (cart == null) {
            throw CartException.cartDoesNotExist();
        }

        for (CartItemEntity item : cart.getItems()) {
            FabricEntity fabric = item.getFabric();
            if (fabric.getStock() <= 5) {
                notifyClientsOfLowStock(fabric);
            }
        }

        return cartMapper.convertToDomain(cart);
    }

    @Transactional
    @Override
    public Cart addItemToCart(Long userId, Long fabricId, int quantity) {
        CartEntity cart = cartRepository.findByUser_Id(userId);
        if (cart == null) {
            cart = createCartForUser(userId);
        }

        FabricEntity fabric = fabricRepository.findById(fabricId)
                .orElseThrow(FabricException::fabricDoesNotExist);

        if (fabric.getStock() < quantity) {
            throw FabricException.insufficientStock();
        }

        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getFabric().getId().equals(fabricId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalPrice(item.getQuantity() * fabric.getPrice());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .fabric(fabric)
                    .quantity(quantity)
                    .totalPrice(quantity * fabric.getPrice())
                    .cart(cart)
                    .build();
            cart.getItems().add(newItem);
        }

        updateCartTotal(cart);
        cartRepository.save(cart);

        return cartMapper.convertToDomain(cart);
    }

    @Transactional
    @Override
    public Cart removeItemFromCart(Long userId, Long fabricId) {
        CartEntity cart = cartRepository.findByUser_Id(userId);

        if (cart == null) {
            throw CartException.cartDoesNotExist();
        }

        Optional<CartItemEntity> itemToRemove = cart.getItems().stream()
                .filter(item -> item.getFabric().getId().equals(fabricId))
                .findFirst();

        if (itemToRemove.isEmpty()) {
            throw CartException.itemNotFoundInCart();
        }

        cart.getItems().remove(itemToRemove.get());
        updateCartTotal(cart);
        cartRepository.save(cart);

        // Notify user about the cart update through WebSocket
        NotificationMessage message = new NotificationMessage();
        message.setFrom("System");
        message.setTo(cart.getUser().getUsername());
        message.setText("Item has been removed from your cart.");
        messagingTemplate.convertAndSendToUser(cart.getUser().getId().toString(),
                "/queue/notification", message);

        FabricEntity fabric = itemToRemove.get().getFabric();
        if (fabric.getStock() <= 5) {
            notifyClientsOfLowStock(fabric);
        }

        return cartMapper.convertToDomain(cart);
    }

    @Transactional
    @Override
    public Cart updateCartFromCart(Long userId, Long fabricId, int quantity) {
        if (quantity <= 0) {
            throw CartException.invalidQuantity();
        }

        CartEntity cart = cartRepository.findByUser_Id(userId);
        if (cart == null) {
            throw CartException.cartDoesNotExist();
        }

        Optional<CartItemEntity> item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getFabric()
                        .getId()
                        .equals(fabricId))
                .findFirst();

        if (item.isEmpty()) {
            throw CartException.itemNotFoundInCart();
        }

        CartItemEntity cartItem = item.get();
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * cartItem.getFabric().getPrice());

        updateCartTotal(cart);
        cartRepository.save(cart);

        return cartMapper.convertToDomain(cart);
    }

    private CartEntity createCartForUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserException::userNotFound);

        CartEntity cart = CartEntity.builder()
                .user(user)
                .items(new ArrayList<>())
                .totalCost(0.0)
                .build();

        return cartRepository.save(cart);
    }

    private void updateCartTotal(CartEntity cart) {
        double totalCost = cart.getItems().stream()
                .mapToDouble(CartItemEntity::getTotalPrice)
                .sum();
        cart.setTotalCost(totalCost);
    }

    @Override
    public void clearCartForUser(Long userId) {
        CartEntity cart = cartRepository.findByUser_Id(userId);

        if (cart != null) {
            cart.getItems().clear();
            cart.setTotalCost(0.0);
            cartRepository.save(cart);
        }
    }

    @Override
    public void notifyClientsOfLowStock(FabricEntity fabric) {
        List<CartEntity> cartsWithFabric = cartRepository.findByFabricId(fabric.getId());

        long count = cartsWithFabric.stream()
                .filter(cart -> cart.getItems().stream()
                        .anyMatch(item -> item.getFabric().getId().equals(fabric.getId())))
                .count();

        for (CartEntity cart : cartsWithFabric) {
            NotificationMessage message = new NotificationMessage();
            message.setFrom("System");
            message.setTo(cart.getUser().getUsername());
            message.setText(fabric.getName()
                    + " is about to be sold out! Only " + fabric.getStock() + " items left. "
                    + count + " people have it in their cart.");
            messagingTemplate.convertAndSendToUser(cart.getUser().getId().toString(),
                    "/queue/notification", message);
        }
    }
}