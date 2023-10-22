package servicecourse.services.bikes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.repo.BikeEntity;
import servicecourse.repo.BikeRepository;
import servicecourse.repo.GroupsetRespository;
import servicecourse.repo.ModelRepository;
import servicecourse.services.EntityFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
    void setup() {
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
        void modelEntityNotFound() {
            // Given
            Long modelId = 1L;

            // When
            when(mockModelRepository.findById(modelId)).thenReturn(Optional.empty());

            // Then
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                         .modelId(modelId.toString())
                                                                         .groupsetName("name")
                                                                         .size("Medium")
                                                                         .build()));
        }

        @Test
        void groupsetEntityNotFound() {
            // Given
            String groupsetName = "name";
            Long modelId = 1L;

            // When
            when(mockModelRepository.findById(modelId))
                    .thenReturn(Optional.of(EntityFactory.newModelEntity()));
            when(mockGroupsetRepository.findById(groupsetName)).thenReturn(Optional.empty());

            // Then
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesService.createBike(CreateBikeInput.newBuilder()
                                                                         .modelId(modelId.toString())
                                                                         .groupsetName(groupsetName)
                                                                         .size("Medium")
                                                                         .build()));
        }
    }
}
