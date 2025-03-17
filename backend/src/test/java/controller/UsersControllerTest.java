package controller;

import org.junit.jupiter.api.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import trendtrack.business.AuthService;
import trendtrack.configuration.security.token.AccessToken;
import trendtrack.domain.user.*;
import static org.mockito.Mockito.*;
import trendtrack.business.UserService;
import trendtrack.TrendTrackApplication;
import org.springframework.http.MediaType;
import trendtrack.controller.UsersController;
import org.junit.jupiter.api.extension.ExtendWith;
import trendtrack.business.exception.UserException;
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

@WebMvcTest(UsersController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin", roles = "ADMIN")
@ContextConfiguration(classes = {TrendTrackApplication.class})
class UsersControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Autowired
    private MockMvc mockMvc;

    // GET /users/{id}
    @Test
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        //arrange
        Long userId = 1L;
        User mockUser = User.builder()
                .id(1L)
                .username("johndoe")
                .password("password")
                .email("johndoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userService.getUser(userId)).thenReturn(mockUser);
        AccessToken mockToken = mock(AccessToken.class);
        when(accessTokenDecoder.decode(anyString())).thenReturn(mockToken);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        //act n assert
        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));

        verify(userService, times(1)).getUser(userId);
        verify(authService, times(1)).validateUser(eq(userId), anyString());
    }

    // GET /users
    @Test
    void getAllUsers_ShouldReturnUsers_WhenUsersExist() throws Exception {
        //arrange
        GetAllUsersResponse mockResponse = new GetAllUsersResponse();
        when(userService.getUsers(any(GetAllUsersRequest.class))).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUsers(any(GetAllUsersRequest.class));
    }

    // GET /users with filters
    @Test
    void getAllUsers_ShouldReturnFilteredUsers_WhenFiltersApplied() throws Exception {
        //arrange
        GetAllUsersResponse mockResponse = new GetAllUsersResponse();
        when(userService.getUsers(any(GetAllUsersRequest.class))).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "johndoe")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUsers(any(GetAllUsersRequest.class));
    }

    // GET /users with pagination
    @Test
    void getAllUsers_ShouldHandlePagination() throws Exception {
        //arrange
        GetAllUsersResponse mockResponse = new GetAllUsersResponse();
        when(userService.getUsers(any(GetAllUsersRequest.class))).thenReturn(mockResponse);

        //act n assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "9"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUsers(any(GetAllUsersRequest.class));
    }

    // GET /users with invalid pagination parameters
    @Test
    void getAllUsers_ShouldReturnBadRequest_WhenInvalidPagination() throws Exception {
        //act n assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "-1")
                        .param("size", "abc"))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).getUsers(any(GetAllUsersRequest.class));
    }

    // POST /users
    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidRequest() throws Exception {
        //arrange
        CreateUserResponse createUserResponse = new CreateUserResponse(
                1L,
                "johndoe",
                "password",
                "johndoe@example.com",
                "John",
                "Doe");

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(createUserResponse);

        //act n assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"johndoe\", \"password\": \"password\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"johndoe@example.com\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    // PUT /users/{id}
    @Test
    void updateUser_ShouldReturnNoContent_WhenValidRequest() throws Exception {
        //arrange
        Long userId = 1L;

        String token = "mock-token";
        AccessToken mockToken = mock(AccessToken.class);
        when(accessTokenDecoder.decode(anyString())).thenReturn(mockToken);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mock(User.class));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        //act n assert
        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"username\": \"johndoe\", \"email\": \"johndoe@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"password\": \"newpassword\"}")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUser(any(UpdateUserRequest.class));
    }

    // DELETE /users/{id}
    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() throws Exception {
        //arrange
        Long userId = 1L;

        //act n assert
        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    // GET /users/{id} user not found
    @Test
    void getUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        //arrange
        Long userId = 1L;

        AccessToken mockToken = mock(AccessToken.class);
        when(accessTokenDecoder.decode(anyString())).thenReturn(mockToken);
        when(userService.getUser(userId)).thenThrow(UserException.userNotFound());

        //act n assert
        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(userId);
        verify(authService, times(1)).validateUser(anyLong(), anyString());
        verify(accessTokenDecoder, times(1)).decode(anyString());
    }

    // POST /users invalid request (missing fields)
    @Test
    void createUser_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        //arrange
        String invalidRequest = "{\"firstName\": \"\", \"lastName\": \"\", \"email\": \"\", \"password\": \"\"}";

        //act n assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).createUser(any(CreateUserRequest.class));
    }
}