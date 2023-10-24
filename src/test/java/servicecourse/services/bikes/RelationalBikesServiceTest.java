package servicecourse.services.bikes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.repo.*;
import servicecourse.services.EntityFactory;
import servicecourse.services.URLFactory;

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
            Long mockModelId = 1L;

            // When no model is returned from the repository
            when(mockModelRepository.findById(mockModelId)).thenReturn(Optional.empty());
            // Note: we don't mock the groupset repository here due to the control flow

            // Then the lack of model should create an error
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                         .modelId(mockModelId.toString())
                                                                         .groupsetName("name")
                                                                         .size("Medium")
                                                                         .build()));

            // Then the bike repository shouldn't have been touched
            verifyNoInteractions(mockBikeRepository);
        }

        @Test
        void groupset_entity_not_found() {
            // Given
            String mockGroupsetName = "name";
            Long mockModelId = 1L;

            // When no groupset is returned from the repository but a model is
            when(mockModelRepository.findById(mockModelId))
                    .thenReturn(Optional.of(EntityFactory.newModelEntity()));
            when(mockGroupsetRepository.findById(mockGroupsetName)).thenReturn(Optional.empty());

            // Then the lack of groupset should create an error
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                         .modelId(mockModelId.toString())
                                                                         .groupsetName(
                                                                                 mockGroupsetName)
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
}
