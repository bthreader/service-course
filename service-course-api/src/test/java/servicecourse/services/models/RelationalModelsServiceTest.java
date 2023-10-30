package servicecourse.services.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.repo.ModelRepository;
import servicecourse.services.EntityFactory;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelationalModelsServiceTest {
    @Mock
    ModelRepository mockModelRepository;
    RelationalModelsService relationalModelsService;

    @BeforeEach
    void setup() {
        relationalModelsService = new RelationalModelsService(mockModelRepository);
    }

    @Nested
    class deleteModel {
        @Test
        void fail_because_no_model() {
            // Given an ID which has no corresponding model
            Long ghostModelId = 0L;
            when(mockModelRepository.findById(ghostModelId)).thenReturn(Optional.empty());

            // When we call the deleteModel method with that ID
            // Then the method should throw
            assertThrows(NoSuchElementException.class,
                         () -> relationalModelsService.deleteModel(ModelId.serialize(ghostModelId)));
        }

        @Test
        void success() {
            // Given an ID which has a corresponding model
            Long modelId = 0L;
            when(mockModelRepository.findById(modelId))
                    .thenReturn(Optional.of(EntityFactory.newModelEntityWithId(modelId)));

            // When we call the deleteModel method with that ID
            Long result = relationalModelsService.deleteModel(ModelId.serialize(modelId));

            // Then the repository should have been asked to delete the model
            verify(mockModelRepository).deleteById(modelId);

            // Then we should receive the ID of the deleted model back
            assertThat(result).isEqualTo(modelId);
        }
    }
}
