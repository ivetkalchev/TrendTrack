package business;

import java.util.*;
import org.mockito.*;
import org.junit.jupiter.api.*;
import trendtrack.domain.user.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import trendtrack.business.mapper.UserMapper;
import trendtrack.persistence.UserRepository;
import trendtrack.business.impl.UserServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import trendtrack.business.exception.UserException;
import trendtrack.persistence.entity.user.RoleEnum;
import trendtrack.persistence.entity.user.UserEntity;
import trendtrack.configuration.security.token.AccessToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AccessToken requestAccessToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers_ShouldReturnAllUsers_WhenRepositoryHasData() {
        //arrange
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .firstName("First")
                .lastName("Last")
                .email("user@example.com")
                .build();

        User user = User.builder()
                .id(1L)
                .username("username")
                .firstName("First")
                .lastName("Last")
                .email("user@example.com")
                .build();

        GetAllUsersRequest request = new GetAllUsersRequest();
        request.setPage(0);
        request.setSize(10);

        when(userRepository.findByFilters(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(Collections.singletonList(userEntity)));

        when(userMapper.convertToDomain(userEntity)).thenReturn(user);

        //act
        GetAllUsersResponse response = userService.getUsers(request);

        //assert
        assertNotNull(response);
        assertEquals(1, response.getUsers().size());
        User returnedUser = response.getUsers().get(0);
        assertEquals("username", returnedUser.getUsername());
        assertEquals("First", returnedUser.getFirstName());
        assertEquals("Last", returnedUser.getLastName());
    }

    @Test
    void getUsers_ShouldReturnNoUsers_WhenNoUsersMatchFilters() {
        //arrange
        GetAllUsersRequest request = new GetAllUsersRequest();
        request.setPage(0);
        request.setSize(10);

        when(userRepository.findByFilters(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                PageRequest.of(0, 10)
        )).thenReturn(Page.empty());

        //act
        GetAllUsersResponse response = userService.getUsers(request);

        //assert
        assertNotNull(response);
        assertEquals(0, response.getUsers().size());
    }

    @Test
    void getUsers_ShouldReturnFilteredUsers_WhenFiltersAreApplied() {
        //arrange
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .firstName("First")
                .lastName("Last")
                .email("user@example.com")
                .build();

        User user = User.builder()
                .id(1L)
                .username("username")
                .firstName("First")
                .lastName("Last")
                .email("user@example.com")
                .build();

        GetAllUsersRequest request = new GetAllUsersRequest();
        request.setUsername("username");
        request.setFirstName("First");
        request.setLastName("Last");
        request.setPage(0);
        request.setSize(10);

        when(userRepository.findByFilters(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(Collections.singletonList(userEntity)));

        when(userMapper.convertToDomain(userEntity)).thenReturn(user);

        //act
        GetAllUsersResponse response = userService.getUsers(request);

        //assert
        assertNotNull(response);
        assertEquals(1, response.getUsers().size());
        User returnedUser = response.getUsers().get(0);
        assertEquals("username", returnedUser.getUsername());
        assertEquals("First", returnedUser.getFirstName());
        assertEquals("Last", returnedUser.getLastName());
    }

    @Test
    void getUsers_ShouldHandlePagination_WhenMultiplePagesExist() {
        //arrange
        UserEntity userEntity1 = UserEntity.builder()
                .id(1L)
                .username("username1")
                .firstName("First1")
                .lastName("Last1")
                .email("user1@example.com")
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .id(2L)
                .username("username2")
                .firstName("First2")
                .lastName("Last2")
                .email("user2@example.com")
                .build();

        User user1 = User.builder()
                .id(1L)
                .username("username1")
                .firstName("First1")
                .lastName("Last1")
                .email("user1@example.com")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("username2")
                .firstName("First2")
                .lastName("Last2")
                .email("user2@example.com")
                .build();

        GetAllUsersRequest request = new GetAllUsersRequest();
        request.setPage(0);
        request.setSize(2);

        when(userRepository.findByFilters(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                PageRequest.of(0, 2)
        )).thenReturn(new PageImpl<>(List.of(userEntity1, userEntity2), PageRequest.of(0, 2), 2));

        when(userMapper.convertToDomain(userEntity1)).thenReturn(user1);
        when(userMapper.convertToDomain(userEntity2)).thenReturn(user2);

        //act
        GetAllUsersResponse response = userService.getUsers(request);

        //assert
        assertNotNull(response);
        assertEquals(2, response.getUsers().size());
        assertEquals("username1", response.getUsers().get(0).getUsername());
        assertEquals("username2", response.getUsers().get(1).getUsername());
    }

    @Test
    void getUser_ShouldThrowUserException_WhenUserDoesNotHaveAccess() {
        //arrange
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(2L);

        //act n assert
        assertThrows(UserException.class, () -> userService.getUser(1L));
    }

    @Test
    void getUser_ShouldThrowUserException_WhenUserDoesNotExist() {
        //arrange
        Long nonExistentUserId = 1L;
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(UserException.class, () -> userService.getUser(nonExistentUserId));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        //arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        //act
        userService.deleteUser(userId);

        //assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowUserException_WhenUserDoesNotExist() {
        //arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        //act n assert
        assertThrows(UserException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void createUser_ShouldCreateNewUser_WhenValidRequest() {
        //arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .username("newuser")
                .firstName("First")
                .lastName("Last")
                .password("Password1!")
                .email("user@example.com")
                .build();

        UserEntity newUserEntity = UserEntity.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password("hashedPassword")
                .build();

        UserEntity savedUserEntity = UserEntity.builder()
                .id(1L)
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password("hashedPassword")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("newuser")
                .firstName("First")
                .lastName("Last")
                .email("user@example.com")
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userMapper.convertToEntity(any(User.class))).thenReturn(newUserEntity);
        when(userRepository.save(newUserEntity)).thenReturn(savedUserEntity);
        when(userMapper.convertToDomain(savedUserEntity)).thenReturn(savedUser);

        //act
        CreateUserResponse response = userService.createUser(request);

        //assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("newuser", response.getUsername());
        assertEquals("First", response.getFirstName());
        assertEquals("Last", response.getLastName());
    }

    @Test
    void createUser_ShouldThrowUserException_WhenUserAlreadyExists() {
        //arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .username("existinguser")
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        //act n assert
        assertThrows(UserException.class, () -> userService.createUser(request));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenValidRequest() {
        //arrange
        Long userId = 1L;

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .username("updateduser")
                .firstName("UpdatedFirst")
                .lastName("UpdatedLast")
                .email("updated@example.com")
                .build();

        UserEntity existingUser = UserEntity.builder()
                .id(userId)
                .username("user")
                .firstName("First")
                .lastName("Last")
                .email("user@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(UserEntity.builder()
                .id(userId)
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build());

        //act
        userService.updateUser(request);

        //assert
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUser_ShouldThrowUserException_WhenUserDoesNotExist() {
        //arrange
        Long userId = 1L;

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .username("updateduser")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //act n assert
        assertThrows(UserException.class, () -> userService.updateUser(request));
    }
}