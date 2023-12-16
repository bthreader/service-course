package servicecourse.repo;

import org.junit.jupiter.api.Test;
import servicecourse.generated.types.BikeBrand;

import static org.assertj.core.api.Assertions.assertThat;

public class BikeBrandEntityTest {
    @Test
    void asBikeBrand() {
        // Given a bike brand
        String name = "name";
        BikeBrandEntity entity = BikeBrandEntity.ofName(name);

        // Then the conversion should be correct
        assertThat(entity.asBikeBrand()).isEqualTo(BikeBrand.newBuilder().name(name).build());
    }
}
