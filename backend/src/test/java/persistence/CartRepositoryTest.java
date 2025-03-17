package persistence;

import java.util.List;
import org.junit.jupiter.api.*;
import trendtrack.persistence.*;
import static org.mockito.Mockito.*;
import trendtrack.domain.fabric.Color;
import trendtrack.TrendTrackApplication;
import trendtrack.domain.fabric.Material;
import trendtrack.persistence.entity.cart.*;
import trendtrack.persistence.entity.user.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import trendtrack.persistence.entity.fabric.FabricEntity;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TrendTrackApplication.class)
public class CartRepositoryTest {

    @MockBean
    private CartRepository cartRepository;

    private UserEntity user;
    private CartEntity cart;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");

        FabricEntity fabric = new FabricEntity();
        fabric.setName("Cotton");
        fabric.setDescription("A high-quality cotton fabric.");
        fabric.setColor(Color.RED);
        fabric.setMaterial(Material.COTTON);
        fabric.setPrice(50.0);
        fabric.setWashable(true);
        fabric.setIroned(true);
        fabric.setStock(100);

        cart = CartEntity.builder()
                .totalCost(100.0)
                .user(user)
                .items(List.of(CartItemEntity.builder()
                        .fabric(fabric)
                        .cart(cart)
                        .quantity(2)
                        .totalPrice(50.0)
                        .build()))
                .build();
    }

    @Test
    void testFindByUserId() {
        //arrange
        when(cartRepository.findByUser_Id(user.getId())).thenReturn(cart);

        //act
        CartEntity foundCart = cartRepository.findByUser_Id(user.getId());

        //assert
        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundCart.getItems()).hasSize(1);
        assertThat(foundCart.getTotalCost()).isEqualTo(100.0);

        verify(cartRepository, times(1)).findByUser_Id(user.getId());
    }

    @Test
    void testFindByUserIdNoCart() {
        //arrange
        UserEntity otherUser = new UserEntity();
        otherUser.setFirstName("Jane");
        otherUser.setLastName("Doe");
        otherUser.setEmail("janedoe@example.com");

        when(cartRepository.findByUser_Id(otherUser.getId())).thenReturn(null);

        //act
        CartEntity foundCart = cartRepository.findByUser_Id(otherUser.getId());

        //assert
        assertThat(foundCart).isNull();

        verify(cartRepository, times(1)).findByUser_Id(otherUser.getId());
    }
}