package servicecourse.services.bikes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.*;
import servicecourse.repo.*;
import servicecourse.repo.common.EntityConstants;
import servicecourse.repo.common.SortUtils;
import servicecourse.services.EntityFactory;
import servicecourse.services.common.exceptions.BikeNotFoundException;
import servicecourse.services.common.exceptions.GroupsetNotFoundException;
import servicecourse.services.common.exceptions.InvalidCursorException;
import servicecourse.services.common.exceptions.ModelNotFoundException;
import servicecourse.utils.Base64Factory;
import servicecourse.utils.Base64ToLongConverter;
import servicecourse.utils.URLFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BikesServiceImplTest {
    @Mock
    BikeRepository mockBikeRepository;
    @Mock
    ModelRepository mockModelRepository;
    @Mock
    GroupsetRespository mockGroupsetRepository;
    BikesServiceImpl bikesService;

    @BeforeEach
    void beforeEach() {
        bikesService = new BikesServiceImpl(mockBikeRepository,
                                            mockModelRepository,
                                            mockGroupsetRepository);
    }

    @Nested
    class bikes {
        @Test
        void single_result_take_one_with_no_after() {
            // Given a `first` and a result
            int numberOfResults = 1;
            int first = 1;
            BikeEntity result = EntityFactory.newBikeEntity();

            // When the repo returns the result
            when(mockBikeRepository.findAll(ArgumentMatchers.<Specification<BikeEntity>>any(),
                                            any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(result),
                                               PageRequest.of(0, first, SortUtils.sortByIdAsc()),
                                               numberOfResults));

            // When we call the method with `first`
            BikeConnection bikeConnection = bikesService.bikes(null, first, null);

            // Then we get the expected result back
            assertThat(bikeConnection.getEdges().stream().map(BikeConnectionEdge::getNode))
                    .isEqualTo(List.of(result.asBike()));

            // Then the page info contains the correct information
            assertThat(bikeConnection.getPageInfo().getHasNextPage()).isFalse();
            assertThat(bikeConnection.getPageInfo().getHasPreviousPage()).isFalse();
        }

        @Test
        void multiple_results_take_one_with_no_after() {
            // Given a `total` that is larger than `first`
            int total = 2;
            int first = 1;
            BikeEntity result = EntityFactory.newBikeEntity();

            // When the repo returns the result
            when(mockBikeRepository.findAll(ArgumentMatchers.<Specification<BikeEntity>>any(),
                                            any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(result),
                                               PageRequest.of(0, first, SortUtils.sortByIdAsc()),
                                               total));

            // When we call the method with `first`
            BikeConnection bikeConnection = bikesService.bikes(null, first, null);

            // Then we get the expected results back
            assertThat(bikeConnection.getEdges().stream().map(BikeConnectionEdge::getNode))
                    .isEqualTo(List.of(result.asBike()));

            // Then the page info contains the correct information
            assertThat(bikeConnection.getPageInfo().getHasNextPage()).isTrue();
            assertThat(bikeConnection.getPageInfo().getHasPreviousPage()).isFalse();
        }

        @Test
        void multiple_results_take_one_with_after() {
            // Given a valid `after`, which has a high enough index for there to be results before
            Long id = EntityConstants.MINIMUM_ID_VALUE + 1L;
            String after = Base64ToLongConverter.encodeToBase64(id);

            // Given a `total` that is larger than `first`
            int total = 2;
            int first = 1;
            BikeEntity result = EntityFactory.newBikeEntity();

            // When the repo returns the result
            when(mockBikeRepository.findAll(ArgumentMatchers.<Specification<BikeEntity>>any(),
                                            any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(result),
                                               PageRequest.of(0, first, SortUtils.sortByIdAsc()),
                                               total));

            // When we call the method with `first` and `after`
            BikeConnection bikeConnection = bikesService.bikes(null,
                                                               first,
                                                               CursorInput.newBuilder()
                                                                       .cursor(after)
                                                                       .build());

            // Then we get the expected result back
            assertThat(bikeConnection.getEdges().stream().map(BikeConnectionEdge::getNode))
                    .isEqualTo(List.of(result.asBike()));

            // Then the page info contains the correct information
            assertThat(bikeConnection.getPageInfo().getHasNextPage()).isTrue();
            assertThat(bikeConnection.getPageInfo().getHasPreviousPage()).isTrue();
        }

        @Test
        void throws_on_bad_after() {
            // Given a string with invalid Base64 characters
            // Then the method should throw
            assertThrows(InvalidCursorException.class,
                         () -> bikesService.bikes(null, 1, CursorInput.newBuilder()
                                 .cursor(Base64Factory.invalidBase64String())
                                 .build()
                         ));

            // Given a string that is valid Base64, but decodes to a string not a long
            // Then the method should throw
            assertThrows(InvalidCursorException.class,
                         () -> bikesService.bikes(null, 1, CursorInput.newBuilder()
                                 .cursor(Base64Factory.stringEncodedAsBase64String())
                                 .build()
                         ));
        }
    }

    @Nested
    class createBike {
        @Test
        void model_entity_not_found() {
            // Given a model id that doesn't exist
            Long ghostModelId = 1L;
            when(mockModelRepository.findById(ghostModelId)).thenReturn(Optional.empty());

            // When trying to create a bike with that model
            // Then createBike should throw
            assertThrows(ModelNotFoundException.class,
                         () -> bikesService.createBike(CreateBikeInput.newBuilder()
                                                               .modelId(BikeId.serialize(
                                                                       ghostModelId))
                                                               .groupsetName("name")
                                                               .size("Medium")
                                                               .build()));
        }

        @Test
        void groupset_entity_not_found() {
            // Given a groupset name which doesn't exist
            String ghostGroupsetName = "ghost";
            when(mockGroupsetRepository.findById(ghostGroupsetName)).thenReturn(Optional.empty());

            // Given a model id which does exist
            Long modelId = 1L;
            when(mockModelRepository.findById(modelId))
                    .thenReturn(Optional.of(EntityFactory.newModelEntity()));

            // When trying to create a bike with that groupset
            // Then createBike should throw
            assertThrows(GroupsetNotFoundException.class,
                         () -> bikesService.createBike(CreateBikeInput.newBuilder()
                                                               .modelId(BikeId.serialize(
                                                                       modelId))
                                                               .groupsetName(
                                                                       ghostGroupsetName)
                                                               .size("Medium")
                                                               .build()));
        }

        @Test
        void successful_save() {
            // Given a model id which exists
            Long mockModelId = 1L;
            ModelEntity mockModelEntity = EntityFactory.newModelEntity();
            when(mockModelRepository.findById(mockModelId)).thenReturn(Optional.of(mockModelEntity));

            // Given a groupset name which exists
            String mockGroupsetName = "name";
            GroupsetEntity mockGroupsetEntity = EntityFactory.newGroupsetEntity();
            when(mockGroupsetRepository.findById(mockGroupsetName)).thenReturn(Optional.of(
                    mockGroupsetEntity));

            // Given other some valid inputs
            String mockSize = "medium";
            URL mockUrl = URLFactory.newUrl().orElseThrow();

            // Given some expected entities (pre and post save)
            BikeEntity.BikeEntityBuilder baseBikeEntityBuilder = BikeEntity.builder()
                    .model(mockModelEntity)
                    .groupset(mockGroupsetEntity)
                    .size(mockSize)
                    .heroImageUrl(mockUrl);
            BikeEntity expectedUnsavedBikeEntity = baseBikeEntityBuilder.id(null).build();
            BikeEntity expectedSavedBikeEntity = baseBikeEntityBuilder.id(1L).build();

            // When the bike repository works as expected
            when(mockBikeRepository.save(expectedUnsavedBikeEntity))
                    .thenReturn(expectedSavedBikeEntity);

            // When we call the createBike method
            Bike result = bikesService.createBike(CreateBikeInput.newBuilder()
                                                          .size(mockSize)
                                                          .groupsetName(mockGroupsetName)
                                                          .modelId(BikeId.serialize(
                                                                  mockModelId))
                                                          .heroImageUrl(mockUrl)
                                                          .build());

            // Then we should have received the expected Bike object
            assertThat(result).isEqualTo(expectedSavedBikeEntity.asBike());
        }
    }

    @Nested
    class updateBike {
        @Test
        void bike_entity_not_found() {
            // Given a bike id which doesn't exist
            Long ghostBikeId = 0L;
            when(mockBikeRepository.findById(ghostBikeId)).thenReturn(Optional.empty());

            // Given an input with that bike id
            UpdateBikeInput input = UpdateBikeInput.newBuilder()
                    .bikeId(BikeId.serialize(ghostBikeId))
                    .build();

            // When we call the updateBike method with this input
            // Then it should throw
            assertThrows(BikeNotFoundException.class,
                         () -> bikesService.updateBike(input));
        }

        @Test
        void groupset_entity_not_found() {
            // Given a bike id which does exist
            Long bikeId = 0L;
            when(mockBikeRepository.findById(bikeId))
                    .thenReturn(Optional.of(EntityFactory.newBikeEntity()));

            // Given a groupset name which doesn't exist
            String ghostGroupsetName = "ghost";
            when(mockGroupsetRepository.findById(ghostGroupsetName))
                    .thenReturn(Optional.empty());

            // Given an input with this bike id and groupset name
            UpdateBikeInput input = UpdateBikeInput.newBuilder()
                    .bikeId("0")
                    .groupsetName(ghostGroupsetName)
                    .build();

            // When we call the updateBike method with this input
            // Then it should throw
            assertThrows(GroupsetNotFoundException.class,
                         () -> bikesService.updateBike(input));
        }

        @Test
        void success() {
            // Given an existing bike
            Long bikeId = 0L;
            BikeEntity oldBikeEntity = EntityFactory.newBikeEntityWithId(bikeId);
            when(mockBikeRepository.findById(bikeId)).thenReturn(Optional.of(oldBikeEntity));

            // Given a groupset name update, where the that new groupset exists
            String groupsetName = "update";
            GroupsetEntity newGroupsetEntity = EntityFactory.newGroupsetEntityWithName("update");
            when(mockGroupsetRepository.findById(groupsetName))
                    .thenReturn(Optional.of(newGroupsetEntity));

            // Given a valid hero image url update
            URL newHeroImageUrl = URLFactory.newUrlWithDomain("update").orElseThrow();

            // Given an input with those updates
            UpdateBikeInput input = UpdateBikeInput.newBuilder()
                    .bikeId(BikeId.serialize(bikeId))
                    .groupsetName(newGroupsetEntity.getName())
                    .heroImageUrl(newHeroImageUrl)
                    .build();

            // Given some expected behaviour
            BikeEntity expectedNewBikeEntity = BikeEntity.builder()
                    .id(oldBikeEntity.getId())
                    .size(oldBikeEntity.getSize())
                    .model(oldBikeEntity.getModel())
                    .groupset(newGroupsetEntity) // Updated
                    .heroImageUrl(newHeroImageUrl) // Updated
                    .build();

            // When we call the updateBike method
            Bike result = bikesService.updateBike(input);

            // Then we should get back the bike we expected
            assertThat(result).isEqualTo(expectedNewBikeEntity.asBike());

            // Then the bike repo should have been asked to save the expected bike entity
            verify(mockBikeRepository).save(expectedNewBikeEntity);
        }
    }

    @Nested
    class deleteBike {
        @Test
        void fail_because_no_bike() {
            // Given an id which has no corresponding bike
            Long ghostBikeId = 0L;
            when(mockBikeRepository.findById(ghostBikeId)).thenReturn(Optional.empty());

            // When we call the deleteBike method with that id
            // Then the method should throw
            assertThrows(BikeNotFoundException.class,
                         () -> bikesService.deleteBike(BikeId.serialize(ghostBikeId)));
        }

        @Test
        void success() {
            // Given an id which has a corresponding bike
            Long bikeId = 0L;
            when(mockBikeRepository.findById(bikeId))
                    .thenReturn(Optional.of(EntityFactory.newBikeEntityWithId(bikeId)));

            // When we call the deleteBike method with that id
            Long result = bikesService.deleteBike(BikeId.serialize(bikeId));

            // Then the repository should have been asked to delete the bike
            verify(mockBikeRepository).deleteById(bikeId);

            // Then we should receive the id of the deleted bike back
            assertThat(result).isEqualTo(bikeId);
        }
    }
}
