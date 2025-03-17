package trendtrack.business;

import trendtrack.domain.fabric.*;

public interface FabricService {

    GetAllFabricResponse getFabrics(GetAllFabricsRequest request);

    Fabric getFabric(Long id);

    CreateFabricResponse createFabric(CreateFabricRequest request);

    void updateFabric(UpdateFabricRequest request);

    void deleteFabric(Long id);
}