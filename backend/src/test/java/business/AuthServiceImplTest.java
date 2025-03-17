package business;

import java.util.*;
import org.mockito.*;
import org.junit.jupiter.api.*;
import trendtrack.domain.user.*;
import trendtrack.persistence.*;
import trendtrack.business.impl.*;
import static org.mockito.Mockito.*;
import trendtrack.business.exception.*;
import trendtrack.persistence.entity.user.*;
import static org.junit.jupiter.api.Assertions.*;
import trendtrack.configuration.security.token.AccessTokenEncoder;
import trendtrack.configuration.security.token.impl.AccessTokenImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldReturnAccessToken_WhenCredentialsAreValid() {
        //arrange
        String username = "username";
        String password = "Password1!";
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .password("$2a$10$exampleEncodedPassword")
                .build();

        Set<UserRoleEntity> mockRoles = new HashSet<>();
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setRole(RoleEnum.CLIENT);
        mockRoles.add(userRole);

        userEntity.setUserRoles(mockRoles);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(Mockito.any(AccessTokenImpl.class)))
                .thenReturn("mockAccessToken");

        //act
        LoginResponse response = authService.login(loginRequest);

        ArgumentCaptor<AccessTokenImpl> captor = ArgumentCaptor.forClass(AccessTokenImpl.class);
        verify(accessTokenEncoder).encode(captor.capture());

        //assert
        assertNotNull(response);
        assertEquals("mockAccessToken", response.getAccessToken());

        AccessTokenImpl capturedAccessToken = captor.getValue();

        assertEquals(1L, capturedAccessToken.getUserId());
    }

    @Test
    void login_ShouldThrowAuthExceptions_WhenUserNotFound() {
        //arrange
        String username = "username";
        String password = "Password1!";
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(AuthException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_ShouldThrowAuthExceptions_WhenPasswordIsInvalid() {
        //arrange
        String username = "username";
        String password = "Password1!";
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .password("$2a$10$exampleEncodedPassword")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(false);

        //act n assert
        assertThrows(AuthException.class, () -> authService.login(loginRequest));
    }
}