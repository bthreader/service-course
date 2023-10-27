package servicecourse.services.bikes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.*;
import servicecourse.repo.*;
import servicecourse.services.models.ModelId;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationalBikesService implements BikesService {
    private final BikeRepository bikeRepository;
    private final ModelRepository modelRepository;
    private final GroupsetRespository groupsetRespository;

    @Override
    public List<Bike> bikes(BikesFilterInput bikesFilterInput) {
        // TODO filtering based on the filter input

        return bikeRepository.findAll()
                .stream()
                .map(BikeEntity::asBike)
                .collect(Collectors.toList());
    }

    @Override
    public Bike createBike(CreateBikeInput createBikeInput) {
        ModelEntity modelEntity = modelRepository.findById(ModelId.deserialize(createBikeInput.getModelId()))
                .orElseThrow(() -> new NoSuchElementException("Model ID not found"));
        GroupsetEntity groupsetEntity = groupsetRespository.findById(createBikeInput.getGroupsetName())
                .orElseThrow(() -> new NoSuchElementException("Groupset not found"));

        BikeEntity newBike = new BikeEntity();
        newBike.apply(CreateBikeParams.builder()
                              .modelEntity(modelEntity)
                              .groupsetEntity(groupsetEntity)
                              .size(createBikeInput.getSize())
                              .heroImageUrl(createBikeInput.getHeroImageUrl())
                              .build());

        return bikeRepository.save(newBike).asBike();
    }

    @Override
    public Bike updateBike(UpdateBikeInput updateBikeInput) {
        return bikeRepository
                .findById(BikeId.deserialize(updateBikeInput.getBikeId()))
                .map((entity) -> {
                    // Pull up the groupset, if it exists
                    Optional<GroupsetEntity> groupsetEntity = Optional.ofNullable(updateBikeInput.getGroupsetName())
                            .flatMap(name -> {
                                Optional<GroupsetEntity> result = groupsetRespository.findById(name);
                                if (result.isEmpty()) {
                                    throw new NoSuchElementException("Groupset not found");
                                }
                                return result;
                            });

                    // Store a copy of the old version of the bike
                    Bike oldBike = entity.asBike();

                    // Apply the input
                    entity.apply(UpdateBikeParams.builder()
                                         .groupset(groupsetEntity)
                                         .heroImageUrl(Optional.ofNullable(updateBikeInput.getHeroImageUrl()))
                                         .build());

                    // Save if there is an update
                    if (!entity.asBike().equals(oldBike)) {
                        bikeRepository.save(entity);
                    }

                    return entity.asBike();
                })
                .orElseThrow(() -> new NoSuchElementException("Bike not found"));
    }

    @Override
    public Optional<Long> deleteBike(DeleteBikeInput deleteBikeInput) {
        Long id = BikeId.deserialize(deleteBikeInput.getBikeId());

        if (bikeRepository.findById(id).isPresent()) {
            bikeRepository.deleteById(id);
            return Optional.of(id);
        }

        return Optional.empty();
    }
}
