package trendtrack.business.mapper;

import org.mapstruct.*;
import trendtrack.domain.user.User;
import trendtrack.persistence.entity.user.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User convertToDomain(UserEntity entity);

    UserEntity convertToEntity(User user);
}