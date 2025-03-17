package persistence;

import java.util.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import trendtrack.TrendTrackApplication;
import org.springframework.data.domain.*;
import trendtrack.persistence.UserRepository;
import org.springframework.data.domain.Pageable;
import trendtrack.persistence.entity.user.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TrendTrackApplication.class)
public class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    private UserEntity user;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setUsername("john_doe");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testExistsByUsername_True() {
        //arrange
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        //act
        boolean exists = userRepository.existsByUsername("john_doe");

        //assert
        assertThat(exists).isTrue();

        verify(userRepository, times(1)).existsByUsername("john_doe");
    }

    @Test
    void testExistsByUsername_False() {
        //arrange
        when(userRepository.existsByUsername("jane_doe")).thenReturn(false);

        //act
        boolean exists = userRepository.existsByUsername("jane_doe");

        //assert
        assertThat(exists).isFalse();

        verify(userRepository, times(1)).existsByUsername("jane_doe");
    }

    @Test
    void testFindByUsername() {
        //arrange
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        //act
        Optional<UserEntity> foundUser = userRepository.findByUsername("john_doe");

        //assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");

        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void testFindByUsername_NotFound() {
        //arrange
        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        //act
        Optional<UserEntity> foundUser = userRepository.findByUsername("unknown_user");

        //assert
        assertThat(foundUser).isEmpty();

        verify(userRepository, times(1)).findByUsername("unknown_user");
    }

    @Test
    void testDeleteById() {
        //arrange
        doNothing().when(userRepository).deleteById(user.getId());

        //act
        userRepository.deleteById(user.getId());

        //assert
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void testExistsById_True() {
        //arrange
        when(userRepository.existsById(user.getId())).thenReturn(true);

        //act
        boolean exists = userRepository.existsById(user.getId());

        //assert
        assertThat(exists).isTrue();

        verify(userRepository, times(1)).existsById(user.getId());
    }

    @Test
    void testExistsById_False() {
        //arrange
        when(userRepository.existsById(user.getId())).thenReturn(false);

        //act
        boolean exists = userRepository.existsById(user.getId());

        //assert
        assertThat(exists).isFalse();

        verify(userRepository, times(1)).existsById(user.getId());
    }

    @Test
    void testFindByFilters_ShouldReturnUsers_WhenFiltersMatch() {
        //arrange
        String usernameFilter = "john_doe";
        String firstNameFilter = "John";
        String lastNameFilter = "Doe";

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(usernameFilter);
        mockUser.setFirstName(firstNameFilter);
        mockUser.setLastName(lastNameFilter);

        when(userRepository.findByFilters(eq(usernameFilter), eq(firstNameFilter), eq(lastNameFilter),
                any(Pageable.class))).thenReturn(new PageImpl<>(List.of(mockUser)));

        //act
        Page<UserEntity> result = userRepository.findByFilters(usernameFilter, firstNameFilter,
                lastNameFilter, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1); // Ensure only one user is returned
        assertThat(result.getContent().get(0).getUsername()).isEqualTo(usernameFilter);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo(firstNameFilter);
        assertThat(result.getContent().get(0).getLastName()).isEqualTo(lastNameFilter);

        verify(userRepository, times(1)).findByFilters(eq(usernameFilter),
                eq(firstNameFilter), eq(lastNameFilter), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnNoUsers_WhenNoFiltersMatch() {
        //arrange
        String usernameFilter = "unknown_user";
        String firstNameFilter = "Unknown";
        String lastNameFilter = "User";

        when(userRepository.findByFilters(eq(usernameFilter), eq(firstNameFilter),
                eq(lastNameFilter), any(Pageable.class))).thenReturn(Page.empty());

        //act
        Page<UserEntity> result = userRepository.findByFilters(usernameFilter, firstNameFilter, lastNameFilter, pageable);

        //assert
        assertThat(result.getContent()).isEmpty();
        verify(userRepository, times(1))
                .findByFilters(eq(usernameFilter), eq(firstNameFilter), eq(lastNameFilter), any(Pageable.class));
    }

    @Test
    void testFindByFilters_ShouldReturnUsers_WhenPartialFiltersMatch() {
        //arrange
        String usernameFilter = "john_doe";
        String firstNameFilter = null;
        String lastNameFilter = null;

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(usernameFilter);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(userRepository.findByFilters(eq(usernameFilter), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockUser)));

        //act
        Page<UserEntity> result = userRepository
                .findByFilters(usernameFilter, firstNameFilter, lastNameFilter, pageable);

        //assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo(usernameFilter);

        verify(userRepository, times(1))
                .findByFilters(eq(usernameFilter), isNull(), isNull(), any(Pageable.class));
    }
}