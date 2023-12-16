package servicecourse.repo;

import org.junit.jupiter.api.Test;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.Model;
import servicecourse.services.models.ModelId;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelEntityTest {
    @Test
    void asModel() {
        // Given some attributes
        Long id = 0L;
        String name = "name";
        int modelYear = 2023;
        String brandName = "brandName";

        // Given an entity with those attributes
        ModelEntity entity = ModelEntity.builder()
                .id(id)
                .name(name)
                .modelYear(modelYear)
                .brandName(brandName)
                .build();

        // Given the equivalent model
        Model model = Model.newBuilder()
                .id(ModelId.serialize(id))
                .name(name)
                .modelYear(modelYear)
                .brand(BikeBrand.newBuilder()
                               .name(brandName)
                               .build())
                .build();

        // Then the method applied on the entity should return the model equivalent
        assertThat(entity.asModel()).isEqualTo(model);
    }
}
