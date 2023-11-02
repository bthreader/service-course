package servicecourse.services.bikes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.URLFactory;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.generated.types.UpdateBikeInput;
import servicecourse.repo.*;
import servicecourse.services.EntityFactory;

import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelationalBikesServiceTest {
    @Mock
    BikeRepository mockBikeRepository;
    @Mock
    ModelRepository mockModelRepository;
    @Mock
    GroupsetRespository mockGroupsetRepository;
    RelationalBikesService relationalBikesService;

    @BeforeEach
    void beforeEach() {
        relationalBikesService = new RelationalBikesService(mockBikeRepository,
                                                            mockModelRepository,
                                                            mockGroupsetRepository);
    }

    @Test
    void bikes() {
        // Given
        BikeEntity result = EntityFactory.newBikeEntity();

        // When
        when(mockBikeRepository.findAll()).thenReturn(List.of(result));
        List<Bike> bikes = relationalBikesService.bikes(null);

        // Then
        assertThat(bikes).isEqualTo(List.of(result.asBike()));
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
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
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
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
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
            Bike result = relationalBikesService.createBike(CreateBikeInput.newBuilder()
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
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.updateBike(input));
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
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.updateBike(input));
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
            Bike result = relationalBikesService.updateBike(input);

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
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.deleteBike(BikeId.serialize(ghostBikeId)));
        }

        @Test
        void success() {
            // Given an id which has a corresponding bike
            Long bikeId = 0L;
            when(mockBikeRepository.findById(bikeId))
                    .thenReturn(Optional.of(EntityFactory.newBikeEntityWithId(bikeId)));

            // When we call the deleteBike method with that id
            Long result = relationalBikesService.deleteBike(BikeId.serialize(bikeId));

            // Then the repository should have been asked to delete the bike
            verify(mockBikeRepository).deleteById(bikeId);

            // Then we should receive the id of the deleted bike back
            assertThat(result).isEqualTo(bikeId);
        }
    }
}
