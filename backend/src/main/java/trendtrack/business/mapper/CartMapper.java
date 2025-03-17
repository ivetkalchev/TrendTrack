package trendtrack.business.mapper;

import java.util.List;
import org.mapstruct.*;
import trendtrack.domain.user.*;
import trendtrack.domain.cart.*;
import trendtrack.persistence.entity.cart.*;
import trendtrack.persistence.entity.user.*;

@Mapper(componentModel = "spring", uses = {FabricMapper.class, UserMapper.class})
public interface CartMapper {

    @Mapping(target = "user", source = "user", qualifiedByName = "mapUser")
    @Mapping(target = "items", source = "items")
    Cart convertToDomain(CartEntity entity);

    List<CartItem> convertCartItemsToDomain(List<CartItemEntity> entities);

    @Mapping(target = "fabric", source = "fabric")
    @Mapping(target = "totalPrice", source = "totalPrice")
    CartItem convertToCartItemDomain(CartItemEntity entity);

    @Named("mapUser")
    default User mapUser(UserEntity userEntity) {
        return userEntity != null ? new UserMapperImpl().convertToDomain(userEntity) : null;
    }
}