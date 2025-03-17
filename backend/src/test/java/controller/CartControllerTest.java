package controller;

import java.util.*;
import org.junit.jupiter.api.*;
import trendtrack.domain.cart.*;
import trendtrack.domain.user.User;
import static org.mockito.Mockito.*;
import trendtrack.business.CartService;
import trendtrack.TrendTrackApplication;
import org.springframework.http.MediaType;
import trendtrack.controller.CartController;
import org.junit.jupiter.api.extension.ExtendWith;
import trendtrack.business.exception.CartException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import trendtrack.configuration.security.token.AccessTokenDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CartController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "client", roles = "CLIENT")
@ContextConfiguration(classes = {TrendTrackApplication.class})
class CartControllerTest {

    @MockBean
    private CartService cartService;

    @MockBean
    private AccessTokenDecoder accessTokenDecode;

    @Autowired
    private MockMvc mockMvc;

    //GET /cart/{userId}
    @Test
    void getCart_ShouldReturnCart_WhenCartExists() throws Exception {
        //arrange
        Long userId = 1L;
        User mockUser = new User();
        CartItem mockCartItem = new CartItem();
        Cart mockCart = new Cart(1L, mockUser, new ArrayList<>(List.of(mockCartItem)), 100.0);
        when(cartService.getCartByUserId(userId)).thenReturn(mockCart);

        //act n assert
        mockMvc.perform(get("/cart/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(100.0));

        verify(cartService, times(1)).getCartByUserId(userId);
    }

    //POST /cart/{userId}/add
    @Test
    void addItemToCart_ShouldReturnUpdatedCart_WhenValidRequest() throws Exception {
        //arrange
        Long userId = 1L;
        CartItem mockCartItem = new CartItem();
        User mockUser = new User();
        Cart mockCart = new Cart(1L, mockUser, new ArrayList<>(List.of(mockCartItem)), 100.0);
        when(cartService.addItemToCart(userId, 1L, 5)).thenReturn(mockCart);

        //act n asser
        mockMvc.perform(post("/cart/{userId}/add", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fabricId\": 1, \"quantity\": 5}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(100.0));

        verify(cartService, times(1)).addItemToCart(userId, 1L, 5);
    }

    //DELETE /cart/{userId}/remove
    @Test
    void removeItemFromCart_ShouldReturnUpdatedCart_WhenItemRemoved() throws Exception {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        CartItem mockCartItem = new CartItem();
        User mockUser = new User();
        Cart mockCart = new Cart(1L, mockUser, new ArrayList<>(List.of(mockCartItem)), 80.0);
        when(cartService.removeItemFromCart(userId, fabricId)).thenReturn(mockCart);

        //act n assert
        mockMvc.perform(delete("/cart/{userId}/remove", userId)
                        .param("fabricId", String.valueOf(fabricId))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(80.0));

        verify(cartService, times(1)).removeItemFromCart(userId, fabricId);
    }

    //PUT /cart/{userId}/update
    @Test
    void updateCartItem_ShouldReturnUpdatedCart_WhenValidRequest() throws Exception {
        //arrange
        Long userId = 1L;
        Long fabricId = 1L;
        CartItem mockCartItem = new CartItem();
        User mockUser = new User();
        Cart mockCart = new Cart(1L, mockUser, new ArrayList<>(List.of(mockCartItem)), 200.0);
        when(cartService.updateCartFromCart(userId, fabricId, 10)).thenReturn(mockCart);

        //act n assert
        mockMvc.perform(put("/cart/{userId}/update", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fabricId\": 1, \"quantity\": 10}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(200.0));

        verify(cartService, times(1)).updateCartFromCart(userId, fabricId, 10);
    }

    //cart does not exist
    @Test
    void getCart_ShouldReturnNotFound_WhenCartDoesNotExist() throws Exception {
        //arrange
        Long userId = 1L;
        when(cartService.getCartByUserId(userId)).thenThrow(CartException.cartDoesNotExist());

        //act n assert
        mockMvc.perform(get("/cart/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).getCartByUserId(userId);
    }

    //invalid quantity in add item request
    @Test
    void addItemToCart_ShouldReturnBadRequest_WhenInvalidQuantity() throws Exception {
        //arrange
        Long userId = 1L;

        //act n assert
        mockMvc.perform(post("/cart/{userId}/add", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fabricId\": 1, \"quantity\": -5}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("quantity"))
                .andExpect(jsonPath("$.errors[0].error").value("must be greater than or equal to 1"));

        verify(cartService, times(0)).addItemToCart(anyLong(), anyLong(), anyInt());
    }
}