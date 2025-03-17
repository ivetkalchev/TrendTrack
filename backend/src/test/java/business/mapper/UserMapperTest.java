package business.mapper;

import org.junit.jupiter.api.*;
import trendtrack.domain.user.User;
import org.mapstruct.factory.Mappers;
import trendtrack.business.mapper.UserMapper;
import trendtrack.persistence.entity.user.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testConvertToDomain() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("john_doe");
        userEntity.setEmail("johndoe@example.com");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");

        User user = userMapper.convertToDomain(userEntity);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userEntity.getId());
        assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userEntity.getLastName());
    }

    @Test
    void testConvertToDomain_Null() {
        UserEntity userEntity = null;

        User user = userMapper.convertToDomain(userEntity);

        assertThat(user).isNull();
    }

    @Test
    void testConvertToEntity() {
        User user = new User(
                1L,
                "john_doe",
                "password",
                "johndoe@example.com",
                "John",
                "Doe");

        UserEntity userEntity = userMapper.convertToEntity(user);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getId()).isEqualTo(user.getId());
        assertThat(userEntity.getUsername()).isEqualTo(user.getUsername());
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        assertThat(userEntity.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userEntity.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    void testConvertToEntity_Null() {
        User user = null;

        UserEntity userEntity = userMapper.convertToEntity(user);

        assertThat(userEntity).isNull();
    }
}