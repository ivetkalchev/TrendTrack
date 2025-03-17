package trendtrack.business.impl;

import lombok.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trendtrack.domain.user.*;
import trendtrack.business.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import trendtrack.persistence.entity.user.*;
import trendtrack.business.mapper.UserMapper;
import trendtrack.persistence.UserRepository;
import org.springframework.stereotype.Service;
import trendtrack.business.exception.UserException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public GetAllUsersResponse getUsers(GetAllUsersRequest request) {
        Page<UserEntity> userPage = userRepository.findByFilters(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                PageRequest.of(request.getPage(), request.getSize())
        );

        List<User> users = userPage.stream()
                .map(userMapper::convertToDomain)
                .toList();

        return GetAllUsersResponse.builder()
                .users(users)
                .build();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::convertToDomain)
                .orElseThrow(UserException::userNotFound);
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw UserException.userAlreadyExists();
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity newUserEntity = userMapper.convertToEntity(User.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build());

        UserRoleEntity clientRole = UserRoleEntity.builder()
                .role(RoleEnum.CLIENT)
                .user(newUserEntity)
                .build();

        newUserEntity.setUserRoles(Set.of(clientRole));
        UserEntity savedUserEntity = userRepository.save(newUserEntity);
        logger.info("New user registered with username: {}", savedUserEntity.getUsername());

        User savedUser = userMapper.convertToDomain(savedUserEntity);

        return CreateUserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw UserException.userNotFound();
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request) {
        UserEntity user = userRepository.findById(request.getId())
                .orElseThrow(UserException::userNotFound);

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userRepository.save(user);
    }
}