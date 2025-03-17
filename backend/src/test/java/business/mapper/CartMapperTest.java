package business.mapper;

import org.mockito.*;
import java.util.List;
import org.junit.jupiter.api.*;
import trendtrack.domain.cart.*;
import trendtrack.domain.user.User;
import trendtrack.business.mapper.*;
import trendtrack.persistence.entity.cart.*;
import trendtrack.persistence.entity.user.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;

public class CartMapperTest {

    @Mock
    private FabricMapper fabricMapper;

    @InjectMocks
    private CartMapperImpl cartMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertToDomain() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("john_doe");
        userEntity.setEmail("johndoe@example.com");

        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(1L);
        cartItemEntity.setQuantity(2);
        cartItemEntity.setTotalPrice(100.0);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(1L);
        cartEntity.setUser(userEntity);
        cartEntity.setItems(List.of(cartItemEntity));

        Cart cart = cartMapper.convertToDomain(cartEntity);

        assertThat(cart).isNotNull();
        assertThat(cart.getId()).isEqualTo(cartEntity.getId());
        assertThat(cart.getUser()).isNotNull();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(cart.getItems().get(0).getTotalPrice()).isEqualTo(100.0);
    }

    @Test
    void testConvertCartItemsToDomain() {
        CartItemEntity cartItemEntity1 = new CartItemEntity();
        cartItemEntity1.setId(1L);
        cartItemEntity1.setQuantity(2);
        cartItemEntity1.setTotalPrice(100.0);

        CartItemEntity cartItemEntity2 = new CartItemEntity();
        cartItemEntity2.setId(2L);
        cartItemEntity2.setQuantity(3);
        cartItemEntity2.setTotalPrice(150.0);

        List<CartItemEntity> cartItemEntities = List.of(cartItemEntity1, cartItemEntity2);

        List<CartItem> cartItems = cartMapper.convertCartItemsToDomain(cartItemEntities);

        assertThat(cartItems).isNotNull();
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems.get(0).getQuantity()).isEqualTo(2);
        assertThat(cartItems.get(1).getQuantity()).isEqualTo(3);
    }

    @Test
    void testConvertToCartItemDomain() {
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(1L);
        cartItemEntity.setQuantity(2);
        cartItemEntity.setTotalPrice(100.0);

        CartItem cartItem = cartMapper.convertToCartItemDomain(cartItemEntity);

        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getQuantity()).isEqualTo(2);
        assertThat(cartItem.getTotalPrice()).isEqualTo(100.0);
    }

    @Test
    void testMapUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("john_doe");
        userEntity.setEmail("johndoe@example.com");

        User user = cartMapper.mapUser(userEntity);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userEntity.getId());
        assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void testMapUser_Null() {
        UserEntity userEntity = null;

        User user = cartMapper.mapUser(userEntity);

        assertThat(user).isNull();
    }

    @Test
    void testConvertToDomain_NullCartEntity() {
        Cart cart = cartMapper.convertToDomain(null);

        assertThat(cart).isNull();
    }

    @Test
    void testConvertToDomain_NullUser() {
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(1L);
        cartItemEntity.setQuantity(2);
        cartItemEntity.setTotalPrice(100.0);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(1L);
        cartEntity.setUser(null);
        cartEntity.setItems(List.of(cartItemEntity));

        Cart cart = cartMapper.convertToDomain(cartEntity);

        assertThat(cart).isNotNull();
        assertThat(cart.getUser()).isNull();
    }
}