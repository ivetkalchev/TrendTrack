package trendtrack.business.mapper;

import org.mapstruct.*;
import trendtrack.domain.order.*;
import trendtrack.persistence.entity.order.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order convertToDomain(OrderEntity entity);

    CreateOrderResponse convertToResponse(OrderEntity entity);
}
