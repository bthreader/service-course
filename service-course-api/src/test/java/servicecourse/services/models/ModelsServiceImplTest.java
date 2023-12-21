package servicecourse.services.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;
import servicecourse.repo.BikeBrandEntity;
import servicecourse.repo.BikeBrandRepository;
import servicecourse.repo.ModelEntity;
import servicecourse.repo.ModelRepository;
import servicecourse.services.EntityFactory;
import servicecourse.services.common.exceptions.BikeBrandNotFoundException;
import servicecourse.services.common.exceptions.ModelNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModelsServiceImplTest {
    @Mock
    ModelRepository mockModelRepository;
    @Mock
    BikeBrandRepository mockBikeBrandRepository;
    ModelsServiceImpl modelsService;

    @BeforeEach
    void setup() {
        modelsService = new ModelsServiceImpl(mockModelRepository,
                                              mockBikeBrandRepository);
    }

    @Nested
    class createModel {
        @Test
        void fail_because_no_bike_brand() {
            // Given a bike brand which doesn't exist
            String ghostBikeBrandName = "ghost";
            when(mockBikeBrandRepository.findById(ghostBikeBrandName)).thenReturn(Optional.empty());

            // Given an otherwise valid input with that bike brand
            CreateModelInput input = CreateModelInput.newBuilder()
                    .name("name")
                    .modelYear(2020)
                    .brandName(ghostBikeBrandName)
                    .build();

            // When we call the createModel method with that bike brand
            // Then the method should throw
            assertThrows(BikeBrandNotFoundException.class,
                         () -> modelsService.createModel(input));
        }

        @Test
        void success() {
            // Given a bike brand which exists
            String bikeBrandName = "Specialized";
            when(mockBikeBrandRepository.findById(bikeBrandName)).thenReturn(Optional.of(
                    BikeBrandEntity.ofName(bikeBrandName)));

            // Given some other inputs
            String name = "name";
            int modelYear = 2020;

            // Given an input with that bike brand
            CreateModelInput input = CreateModelInput.newBuilder()
                    .name(name)
                    .modelYear(modelYear)
                    .brandName(bikeBrandName)
                    .build();

            // Given some expected entities (pre and post save)
            ModelEntity.ModelEntityBuilder baseModelEntityBuilder = ModelEntity.builder()
                    .brandName(bikeBrandName)
                    .modelYear(modelYear)
                    .name(name);
            ModelEntity expectedUnsavedModelEntity = baseModelEntityBuilder.id(null).build();
            ModelEntity expectedSavedModelEntity = baseModelEntityBuilder.id(1L).build();

            // When the model repository works as expected
            when(mockModelRepository.save(expectedUnsavedModelEntity))
                    .thenReturn(expectedSavedModelEntity);

            // When we call the createModel method with that input
            Model result = modelsService.createModel(input);

            // Then we should have received the expected Model object
            assertThat(result).isEqualTo(expectedSavedModelEntity.asModel());
        }
    }

    @Nested
    class deleteModel {
        @Test
        void fail_because_no_model() {
            // Given an id which has no corresponding model
            Long ghostModelId = 0L;
            when(mockModelRepository.findById(ghostModelId)).thenReturn(Optional.empty());

            // When we call the deleteModel method with that id
            // Then the method should throw
            assertThrows(ModelNotFoundException.class,
                         () -> modelsService.deleteModel(ModelId.serialize(ghostModelId)));
        }

        @Test
        void success() {
            // Given an id which has a corresponding model
            Long modelId = 0L;
            when(mockModelRepository.findById(modelId))
                    .thenReturn(Optional.of(EntityFactory.newModelEntityWithId(modelId)));

            // When we call the deleteModel method with that id
            String result = modelsService.deleteModel(ModelId.serialize(modelId));

            // Then the repository should have been asked to delete the model
            verify(mockModelRepository).deleteById(modelId);

            // Then we should receive the id of the deleted model back
            assertThat(result).isEqualTo(ModelId.serialize(modelId));
        }
    }
}
