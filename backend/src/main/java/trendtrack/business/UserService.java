package trendtrack.business;

import trendtrack.domain.user.*;

public interface UserService {

    GetAllUsersResponse getUsers(GetAllUsersRequest request);

    User getUser(Long id);

    CreateUserResponse createUser(CreateUserRequest request);

    void updateUser(UpdateUserRequest request);

    void deleteUser(Long id);
}