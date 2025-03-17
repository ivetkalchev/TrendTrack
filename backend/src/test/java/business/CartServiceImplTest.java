package business;

import java.util.*;
import org.mockito.*;
import org.junit.jupiter.api.*;
import java.lang.reflect.Method;
import trendtrack.domain.cart.*;
import trendtrack.persistence.*;
import trendtrack.business.impl.*;
import static org.mockito.Mockito.*;
import trendtrack.business.exception.*;
import trendtrack.persistence.entity.cart.*;
import trendtrack.business.mapper.CartMapper;
import static org.junit.jupiter.api.Assertions.*;
import trendtrack.persistence.entity.user.UserEntity;
import trendtrack.persistence.entity.fabric.FabricEntity;

public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private FabricRepository fabricRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private UserEntity mockUserEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCartByUserId_ShouldReturnCart_WhenCartExists() {
        //arrange
        Long userId = 1L;
        CartEntity cartEntity = new CartEntity();
        cartEntity.setUser(mockUserEntity);
        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);
        when(cartMapper.convertToDomain(cartEntity)).thenReturn(new Cart());

        //act
        Cart result = cartService.getCartByUserId(userId);

        //assert
        assertNotNull(result);
        verify(cartRepository).findByUser_Id(userId);
    }

    @Test
    void getCartByUserId_ShouldThrowCartExceptions_WhenCartDoesNotExist() {
        //arrange
        Long userId = 1L;
        when(cartRepository.findByUser_Id(userId)).thenReturn(null);

        //act n assert
        assertThrows(CartException.class, () -> cartService.getCartByUserId(userId));
    }

    @Test
    void addItemToCart_ShouldAddItem_WhenItemDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 2;

        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());
        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setId(fabricId);
        fabricEntity.setPrice(10.0);
        fabricEntity.setStock(10);

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);
        when(fabricRepository.findById(fabricId)).thenReturn(Optional.of(fabricEntity));
        when(cartMapper.convertToDomain(cartEntity)).thenReturn(new Cart());

        //act
        Cart result = cartService.addItemToCart(userId, fabricId, quantity);

        //assert
        assertNotNull(result);
        assertEquals(1, cartEntity.getItems().size());
        verify(cartRepository).save(cartEntity);
    }

    @Test
    void removeItemFromCart_ShouldThrowCartExceptions_WhenItemDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;

        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);

        //act n assert
        assertThrows(CartException.class,
                () -> cartService.removeItemFromCart(userId, fabricId));
    }

    @Test
    void updateCartFromCart_ShouldUpdateQuantity_WhenItemExists() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 5;

        CartEntity cartEntity = new CartEntity();
        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setId(fabricId);
        fabricEntity.setPrice(10.0);
        CartItemEntity cartItemEntity = new CartItemEntity();

        cartItemEntity.setFabric(fabricEntity);
        cartItemEntity.setQuantity(2);
        cartItemEntity.setTotalPrice(fabricEntity.getPrice() * cartItemEntity.getQuantity());

        cartEntity.getItems().add(cartItemEntity);

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);
        when(fabricRepository.findById(fabricId)).thenReturn(Optional.of(fabricEntity));
        when(cartMapper.convertToDomain(cartEntity)).thenReturn(new Cart());

        //act
        Cart result = cartService.updateCartFromCart(userId, fabricId, quantity);

        //assert
        assertNotNull(result);
        assertEquals(50.0, cartItemEntity.getTotalPrice());
        verify(cartRepository).save(cartEntity);
    }

    @Test
    void updateCartFromCart_ShouldThrowCartExceptions_WhenItemDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 5;

        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);

        //act n assert
        assertThrows(CartException.class,
                () -> cartService.updateCartFromCart(userId, fabricId, quantity));
    }

    @Test
    void clearCartForUser_ShouldClearCart_WhenCartExists() {
        //arrange
        Long userId = 1L;

        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);

        //act
        cartService.clearCartForUser(userId);

        //assert
        assertTrue(cartEntity.getItems().isEmpty());
        verify(cartRepository).save(cartEntity);
    }

    @Test
    void clearCartForUser_ShouldDoNothing_WhenCartDoesNotExist() {
        //arrange
        Long userId = 1L;

        when(cartRepository.findByUser_Id(userId)).thenReturn(null);

        //act
        cartService.clearCartForUser(userId);

        //assert
        verify(cartRepository, never()).save(any(CartEntity.class));
    }

    @Test
    void addItemToCart_ShouldThrowFabricException_WhenFabricNotFound() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 2;
        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);
        when(fabricRepository.findById(fabricId)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(FabricException.class, () -> cartService.addItemToCart(userId, fabricId, quantity));
    }

    @Test
    void addItemToCart_ShouldAddMultipleItems_WhenMultipleItemsAreAdded() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity1 = 2;
        int quantity2 = 3;

        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());
        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setId(fabricId);
        fabricEntity.setPrice(10.0);
        fabricEntity.setStock(10);

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);
        when(fabricRepository.findById(fabricId)).thenReturn(Optional.of(fabricEntity));
        when(cartMapper.convertToDomain(cartEntity)).thenReturn(new Cart());

        //act
        Cart result1 = cartService.addItemToCart(userId, fabricId, quantity1);
        Cart result2 = cartService.addItemToCart(userId, fabricId, quantity2);

        //assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(50.0, cartEntity.getTotalCost(),
                "Total cost should be correct after adding multiple items");
        verify(cartRepository, times(2)).save(cartEntity);
    }

    @Test
    void updateCartFromCart_ShouldThrowInvalidQuantity_WhenQuantityIsZero() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 0; // invalid quantity
        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(new ArrayList<>());

        when(cartRepository.findByUser_Id(userId)).thenReturn(cartEntity);

        //act n assert
        assertThrows(CartException.class, () -> cartService.updateCartFromCart(userId, fabricId, quantity));
    }

    @Test
    void addItemToCart_ShouldThrowCartException_WhenCartDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 2;
        when(cartRepository.findByUser_Id(userId)).thenReturn(null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(UserException.class, () -> cartService.addItemToCart(userId, fabricId, quantity),
                "Expected UserException to be thrown for non-existing user");
    }

    @Test
    void getCartByUserId_ShouldReturnCorrectCartForMultipleUsers() {
        //arrange
        Long userId1 = 1L;
        Long userId2 = 2L;
        CartEntity cartEntity1 = new CartEntity();
        cartEntity1.setUser(mockUserEntity);
        CartEntity cartEntity2 = new CartEntity();
        cartEntity2.setUser(mockUserEntity);

        when(cartRepository.findByUser_Id(userId1)).thenReturn(cartEntity1);
        when(cartRepository.findByUser_Id(userId2)).thenReturn(cartEntity2);
        when(cartMapper.convertToDomain(cartEntity1)).thenReturn(new Cart());
        when(cartMapper.convertToDomain(cartEntity2)).thenReturn(new Cart());

        //act
        Cart result1 = cartService.getCartByUserId(userId1);
        Cart result2 = cartService.getCartByUserId(userId2);

        //assert
        assertNotNull(result1);
        assertNotNull(result2);
    }

    @Test
    void addItemToCart_ShouldCreateCart_WhenCartDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 2;

        UserEntity mockUserEntity = mock(UserEntity.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserEntity));

        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setId(fabricId);
        fabricEntity.setStock(10);

        when(cartRepository.findByUser_Id(userId)).thenReturn(null);
        when(fabricRepository.findById(fabricId)).thenReturn(Optional.of(fabricEntity));
        when(cartMapper.convertToDomain(any(CartEntity.class))).thenReturn(new Cart());
        when(cartRepository.save(any(CartEntity.class))).thenReturn(new CartEntity());

        //act
        Cart result = cartService.addItemToCart(userId, fabricId, quantity);

        //assert
        assertNotNull(result);
        verify(cartRepository, times(2)).save(any(CartEntity.class));
    }

    @Test
    void addItemToCart_ShouldThrowFabricException_WhenInsufficientStock() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 100;

        FabricEntity fabricEntity = new FabricEntity();
        fabricEntity.setId(fabricId);
        fabricEntity.setStock(10);

        when(cartRepository.findByUser_Id(userId)).thenReturn(new CartEntity());
        when(fabricRepository.findById(fabricId)).thenReturn(Optional.of(fabricEntity));

        //act n assert
        assertThrows(FabricException.class, () -> cartService.addItemToCart(userId, fabricId, quantity),
                "Expected FabricException to be thrown for insufficient stock");
    }

    @Test
    void removeItemFromCart_ShouldThrowCartException_WhenCartDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;

        when(cartRepository.findByUser_Id(userId)).thenReturn(null);

        //act n assert
        assertThrows(CartException.class, () -> cartService.removeItemFromCart(userId, fabricId),
                "Expected CartException to be thrown when cart does not exist");
    }

    @Test
    void updateCartFromCart_ShouldThrowCartException_WhenCartDoesNotExist() {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        int quantity = 2;

        when(cartRepository.findByUser_Id(userId)).thenReturn(null);

        //act n assert
        assertThrows(CartException.class, () -> cartService.updateCartFromCart(userId, fabricId, quantity),
                "Expected CartException to be thrown for non-existing cart");
    }
}