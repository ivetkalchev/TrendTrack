package trendtrack.business.impl;

import lombok.*;
import java.util.List;
import trendtrack.business.*;
import trendtrack.domain.fabric.*;
import org.springframework.data.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import trendtrack.persistence.FabricRepository;
import trendtrack.business.mapper.FabricMapper;
import trendtrack.business.exception.FabricException;
import trendtrack.persistence.entity.fabric.FabricEntity;

@Service
@AllArgsConstructor
public class FabricServiceImpl implements FabricService {
    private final FabricMapper fabricMapper;
    private final FabricRepository fabricRepository;

    @Override
    public GetAllFabricResponse getFabrics(GetAllFabricsRequest request) {
        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<FabricEntity> fabricPage = fabricRepository.findByFilters(
                request.getName(),
                request.getMaterial(),
                request.getColor(),
                request.getMinPrice(),
                request.getMaxPrice(),
                pageable
        );

        List<Fabric> fabrics = fabricPage.stream()
                .map(fabricMapper::convertToDomain)
                .toList();

        return GetAllFabricResponse.builder()
                .fabrics(fabrics)
                .build();
    }

    @Override
    public Fabric getFabric(Long id) {
        return fabricRepository.findById(id)
                .map(fabricMapper::convertToDomain)
                .orElseThrow(FabricException::fabricDoesNotExist);
    }

    @Override
    @Transactional
    public CreateFabricResponse createFabric(CreateFabricRequest request) {
        if (fabricRepository.existsByName(request.getName())) {
            throw FabricException.fabricAlreadyExists();
        }

        FabricEntity newFabric = FabricEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .material(request.getMaterial())
                .color(request.getColor())
                .price(request.getPrice())
                .washable(request.isWashable())
                .ironed(request.isIroned())
                .stock(request.getStock())
                .pictureUrl(request.getPictureUrl())
                .build();

        FabricEntity savedFabric = fabricRepository.save(newFabric);

        return CreateFabricResponse.builder()
                .id(savedFabric.getId())
                .name(savedFabric.getName())
                .description(savedFabric.getDescription())
                .material(savedFabric.getMaterial())
                .color(savedFabric.getColor())
                .price(savedFabric.getPrice())
                .washable(savedFabric.isWashable())
                .ironed(savedFabric.isIroned())
                .stock(savedFabric.getStock())
                .pictureUrl(savedFabric.getPictureUrl())
                .build();
    }

    @Override
    @Transactional
    public void updateFabric(UpdateFabricRequest request) {
        FabricEntity fabric = fabricRepository.findById(request.getId())
                .orElseThrow(FabricException::fabricDoesNotExist);

        fabric.setName(request.getName());
        fabric.setDescription(request.getDescription());
        fabric.setMaterial(request.getMaterial());
        fabric.setColor(request.getColor());
        fabric.setPrice(request.getPrice());
        fabric.setWashable(request.isWashable());
        fabric.setIroned(request.isIroned());
        fabric.setStock(request.getStock());
        fabric.setPictureUrl(request.getPictureUrl());

        fabricRepository.save(fabric);
    }

    @Override
    @Transactional
    public void deleteFabric(Long id) {
        if (!fabricRepository.existsById(id)) {
            throw FabricException.fabricDoesNotExist();
        }
        fabricRepository.deleteById(id);
    }
}