package servicecourse.services.bikes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.generated.types.UpdateBikeInput;
import servicecourse.repo.*;
import servicecourse.services.exceptions.BikeNotFoundException;
import servicecourse.services.exceptions.GroupsetNotFoundException;
import servicecourse.services.exceptions.ModelNotFoundException;
import servicecourse.services.models.ModelId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BikesServiceImpl implements BikesService {
    private final BikeRepository bikeRepository;
    private final ModelRepository modelRepository;
    private final GroupsetRespository groupsetRespository;

    @Override
    public List<Bike> bikes(BikesFilterInput filter) {
        if (filter == null) {
            return bikes();
        }

        return bikeRepository.findAll(BikeEntitySpecification.from(filter))
                .stream()
                .map(BikeEntity::asBike)
                .collect(Collectors.toList());
    }

    private List<Bike> bikes() {
        return bikeRepository.findAll()
                .stream()
                .map(BikeEntity::asBike)
                .collect(Collectors.toList());
    }

    @Override
    public Bike createBike(CreateBikeInput input) {
        ModelEntity modelEntity = modelRepository.findById(ModelId.deserialize(input.getModelId()))
                .orElseThrow(() -> new ModelNotFoundException(input.getModelId()));
        GroupsetEntity groupsetEntity = groupsetRespository.findById(input.getGroupsetName())
                .orElseThrow(() -> new GroupsetNotFoundException(input.getGroupsetName()));

        BikeEntity newBike = BikeEntity.builder()
                .model(modelEntity)
                .groupset(groupsetEntity)
                .size(input.getSize())
                .heroImageUrl(input.getHeroImageUrl())
                .build();

        return bikeRepository.save(newBike).asBike();
    }

    @Override
    public Bike updateBike(UpdateBikeInput input) {
        return bikeRepository
                .findById(BikeId.deserialize(input.getBikeId()))
                .map((entity) -> {
                    // Pull up the groupset, if it has been specified
                    Optional<GroupsetEntity> groupsetEntity = Optional.ofNullable(input.getGroupsetName())
                            .flatMap(name -> {
                                Optional<GroupsetEntity> result = groupsetRespository.findById(name);
                                if (result.isEmpty()) {
                                    throw new GroupsetNotFoundException(name);
                                }
                                return result;
                            });

                    // Store a copy of the old version of the bike
                    Bike oldBike = entity.asBike();

                    // Apply the input
                    entity.apply(UpdateBikeParams.builder()
                                         .groupset(groupsetEntity)
                                         .heroImageUrl(input.getHeroImageUrl())
                                         .build());

                    // Save if there is an update
                    if (!entity.asBike().equals(oldBike)) {
                        bikeRepository.save(entity);
                    }

                    return entity.asBike();
                })
                .orElseThrow(() -> new BikeNotFoundException(input.getBikeId()));
    }

    @Override
    public Long deleteBike(String id) {
        return bikeRepository.findById(BikeId.deserialize(id))
                .map((entity) -> {
                    bikeRepository.deleteById(entity.getId());
                    return entity.getId();
                })
                .orElseThrow(() -> new BikeNotFoundException(id));
    }
}
