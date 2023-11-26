package servicecourse.services.bikes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.generated.types.UpdateBikeInput;
import servicecourse.repo.*;
import servicecourse.services.Errors;
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
        // TODO filtering based on the filter input

        return bikeRepository.findAll()
                .stream()
                .map(BikeEntity::asBike)
                .collect(Collectors.toList());
    }

    @Override
    public Bike createBike(CreateBikeInput input) {
        ModelEntity modelEntity = modelRepository.findById(ModelId.deserialize(input.getModelId()))
                .orElseThrow(Errors::newModelNotFoundError);
        GroupsetEntity groupsetEntity = groupsetRespository.findById(input.getGroupsetName())
                .orElseThrow(Errors::newGroupsetNotFoundError);

        BikeEntity newBike = new BikeEntity();
        newBike.apply(CreateBikeParams.builder()
                              .modelEntity(modelEntity)
                              .groupsetEntity(groupsetEntity)
                              .size(input.getSize())
                              .heroImageUrl(input.getHeroImageUrl())
                              .build());

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
                                    throw Errors.newGroupsetNotFoundError();
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
                .orElseThrow(Errors::newBikeNotFoundError);
    }

    @Override
    public Long deleteBike(String id) {
        return bikeRepository.findById(BikeId.deserialize(id))
                .map((entity) -> {
                    bikeRepository.deleteById(entity.getId());
                    return entity.getId();
                })
                .orElseThrow(Errors::newBikeNotFoundError);
    }
}
