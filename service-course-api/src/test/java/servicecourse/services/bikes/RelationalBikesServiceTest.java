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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RelationalBikesServiceTest {
    @Mock
    private BikeRepository mockBikeRepository;
    @Mock
    private ModelRepository mockModelRepository;
    @Mock
    private GroupsetRespository mockGroupsetRepository;
    private RelationalBikesService relationalBikesService;

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
            // Given
            Long ghostModelId = 1L;

            // When no model is returned from the repository
            when(mockModelRepository.findById(ghostModelId)).thenReturn(Optional.empty());

            // Then the lack of model should create an error
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                         .modelId(ghostModelId.toString())
                                                                         .groupsetName("name")
                                                                         .size("Medium")
                                                                         .build()));

            // Then the bike repository shouldn't have been touched
            verifyNoInteractions(mockBikeRepository);
        }

        @Test
        void groupset_entity_not_found() {
            // Given
            String ghostGroupsetName = "ghost";
            Long mockModelId = 1L;

            // When no groupset is returned from the repository, but a model is
            when(mockGroupsetRepository.findById(ghostGroupsetName)).thenReturn(Optional.empty());
            when(mockModelRepository.findById(mockModelId))
                    .thenReturn(Optional.of(EntityFactory.newModelEntity()));

            // Then the lack of groupset should create an error
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                         .modelId(mockModelId.toString())
                                                                         .groupsetName(
                                                                                 ghostGroupsetName)
                                                                         .size("Medium")
                                                                         .build()));

            // Then the bike repository shouldn't have been touched
            verifyNoInteractions(mockBikeRepository);
        }

        @Test
        void successful_save() {
            // Given a "valid" model
            Long mockModelId = 1L;
            ModelEntity mockModelEntity = EntityFactory.newModelEntity();
            when(mockModelRepository.findById(mockModelId)).thenReturn(Optional.of(mockModelEntity));

            // Given a "valid" groupset
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
            when(mockBikeRepository.save(any())).thenReturn(expectedSavedBikeEntity);

            // When we call the createBike method
            Bike result = relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                    .size(mockSize)
                                                                    .groupsetName(mockGroupsetName)
                                                                    .modelId(mockModelId.toString())
                                                                    .heroImageUrl(mockUrl)
                                                                    .build());

            // Then the bike repository should have been asked to save the expected BikeEntity
            verify(mockBikeRepository).save(expectedUnsavedBikeEntity);

            // Then we should have received the expected Bike object
            assertThat(result).isEqualTo(expectedSavedBikeEntity.asBike());
        }
    }

    @Nested
    class updateBike {
        @Test
        void bike_entity_not_found() {
            // Given a bike ID which doesn't exist
            Long ghostBikeId = 0L;
            when(mockBikeRepository.findById(ghostBikeId)).thenReturn(Optional.empty());

            // Given an UpdateBikeInput with that bike ID
            UpdateBikeInput updateBikeInput = UpdateBikeInput.newBuilder()
                    .bikeId(ghostBikeId.toString())
                    .build();

            // When we call the updateBike method with this input
            // Then it should throw
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.updateBike(updateBikeInput));
        }

        @Test
        void groupset_entity_not_found() {
            // Given a bike ID which does exist
            Long bikeId = 0L;
            when(mockBikeRepository.findById(bikeId))
                    .thenReturn(Optional.of(EntityFactory.newBikeEntity()));

            // Given a groupset name which doesn't exist
            String ghostGroupsetName = "ghost";
            when(mockGroupsetRepository.findById(ghostGroupsetName))
                    .thenReturn(Optional.empty());

            // Given an UpdateBikeInput with this bike ID and groupset name
            UpdateBikeInput updateBikeInput = UpdateBikeInput.newBuilder()
                    .bikeId("0")
                    .groupsetName(ghostGroupsetName)
                    .build();

            // When we call the updateBike method with this input
            // Then it should throw
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.updateBike(updateBikeInput));
        }

        @Test
        void success() {
            // Given an existing bike
            Long bikeId = 0L;
            BikeEntity oldBikeEntity = EntityFactory.newBikeEntityWithId(bikeId);
            when(mockBikeRepository.findById(bikeId)).thenReturn(Optional.of(oldBikeEntity));

            // Given a groupset update, where the groupset exists
            String groupsetName = "update";
            GroupsetEntity newGroupsetEntity = EntityFactory.newGroupsetEntityWithName("update");
            when(mockGroupsetRepository.findById(groupsetName))
                    .thenReturn(Optional.of(newGroupsetEntity));

            // Given a valid hero image url update
            URL newHeroImageUrl = URLFactory.newUrlWithDomain("update").orElseThrow();

            // Given an UpdateBikeInput with those updates
            UpdateBikeInput updateBikeInput = UpdateBikeInput.newBuilder()
                    .bikeId(bikeId.toString())
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

            // Then we should get back the bike we expected
            assertThat(relationalBikesService.updateBike(updateBikeInput)).isEqualTo(
                    expectedNewBikeEntity.asBike());

            // Then the bike repo should have been asked to save the expected bike entity
            verify(mockBikeRepository).save(expectedNewBikeEntity);
        }
    }
}
