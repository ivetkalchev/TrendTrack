package trendtrack.business.mapper;

import org.mapstruct.Mapper;
import trendtrack.domain.fabric.Fabric;
import trendtrack.persistence.entity.fabric.FabricEntity;

@Mapper(componentModel = "spring")
public interface FabricMapper {

    Fabric convertToDomain(FabricEntity entity);
}