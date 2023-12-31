package servicecourse.services.bikes;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.*;
import servicecourse.repo.*;
import servicecourse.repo.common.LongFilterSpecification;
import servicecourse.repo.common.SortUtils;
import servicecourse.services.common.CursorName;
import servicecourse.services.common.CursorUtils;
import servicecourse.services.common.exceptions.BikeNotFoundException;
import servicecourse.services.common.exceptions.GroupsetNotFoundException;
import servicecourse.services.common.exceptions.ModelNotFoundException;
import servicecourse.services.models.ModelId;

import java.util.Optional;

import static servicecourse.repo.common.EntityConstants.MINIMUM_ID_VALUE;

@Service
@RequiredArgsConstructor
public class BikesServiceImpl implements BikesService {
    private final BikeRepository bikeRepository;
    private final ModelRepository modelRepository;
    private final GroupsetRespository groupsetRespository;
    private static final int MAXIMUM_FIRST_VALUE = 100;

    @Override
    public BikeConnection bikes(final BikesFilterInput filter, final int first,
                                @Nullable final CursorInput after) {
        if (first < 1) {
            throw new IllegalArgumentException("First must be greater than zero");
        }

        final Optional<Long> afterId = Optional.ofNullable(after)
                .map(CursorInput::getCursor)
                .map(cursor -> CursorUtils.decodeBase64CursorToLong(CursorName.AFTER, cursor));

        // If specified, the after cursor is the ID of the last bike seen
        // Bikes are always returned to the client with IDs in ascending order
        // Therefore, if `after` is present, our query is only interested in IDs after this ID
        final Optional<Specification<BikeEntity>> afterSpecification = afterId
                .map(id -> LongFilterSpecification.newGreaterThanSpecification(id, BikeEntity_.id));

        final Specification<BikeEntity> defaultSpecification = BikeEntitySpecification.from(filter);

        final Specification<BikeEntity> specification = afterSpecification
                .map(s -> Specification.allOf(defaultSpecification, s))
                .orElse(defaultSpecification);

        // Run the query
        final Page<BikeEntity> page = bikeRepository.findAll(specification,
                                                             PageRequest.of(0,
                                                                            Math.min(first,
                                                                                     MAXIMUM_FIRST_VALUE),
                                                                            SortUtils.sortByIdAsc()));

        return BikeConnection.newBuilder()
                .edges(page.get()
                               .map(BikeEntity::asBikeConnectionEdge)
                               .toList())
                .pageInfo(PageInfo.newBuilder()
                                  .hasNextPage(page.hasNext())
                                  .hasPreviousPage(afterId.map(id -> id >= MINIMUM_ID_VALUE)
                                                           .orElse(false))
                                  .startCursor(CursorUtils.startCursor(page, BikeEntity::base64Id)
                                                       .orElse(null))
                                  .endCursor(CursorUtils.endCursor(page, BikeEntity::base64Id)
                                                     .orElse(null))
                                  .build()
                ).build();
    }

    @Override
    public Bike createBike(final CreateBikeInput input) {
        final ModelEntity modelEntity = modelRepository.findById(ModelId.deserialize(input.getModelId()))
                .orElseThrow(() -> new ModelNotFoundException(input.getModelId()));
        final GroupsetEntity groupsetEntity = groupsetRespository.findById(input.getGroupsetName())
                .orElseThrow(() -> new GroupsetNotFoundException(input.getGroupsetName()));

        final BikeEntity newBike = BikeEntity.builder()
                .model(modelEntity)
                .groupset(groupsetEntity)
                .size(input.getSize())
                .heroImageUrl(input.getHeroImageUrl())
                .build();

        return bikeRepository.save(newBike).asBike();
    }

    @Override
    public Bike updateBike(final UpdateBikeInput input) {
        return bikeRepository
                .findById(BikeId.deserialize(input.getBikeId()))
                .map((entity) -> {
                    // Pull up the groupset, if it has been specified
                    final Optional<GroupsetEntity> groupsetEntity = Optional.ofNullable(input.getGroupsetName())
                            .flatMap(name -> {
                                final Optional<GroupsetEntity> result = groupsetRespository.findById(
                                        name);
                                if (result.isEmpty()) {
                                    throw new GroupsetNotFoundException(name);
                                }
                                return result;
                            });

                    // Store a copy of the old version of the bike
                    final Bike oldBike = entity.asBike();

                    // Apply the input
                    entity.apply(UpdateBikeParams.builder()
                                         .groupset(groupsetEntity.orElse(null))
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
    public Long deleteBike(final String id) {
        return bikeRepository.findById(BikeId.deserialize(id))
                .map((entity) -> {
                    bikeRepository.deleteById(entity.getId());
                    return entity.getId();
                })
                .orElseThrow(() -> new BikeNotFoundException(id));
    }
}
