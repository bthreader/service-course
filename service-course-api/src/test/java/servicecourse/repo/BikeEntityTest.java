package servicecourse.repo;

import org.junit.jupiter.api.Test;
import servicecourse.URLFactory;
import servicecourse.generated.types.Bike;
import servicecourse.services.EntityFactory;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class BikeEntityTest {
    @Test
    void asBike() {
        // Given a full set of attributes
        Long id = 0L;
        String size = "medium";
        URL heroImageUrl = URLFactory.newUrl().orElseThrow();
        ModelEntity modelEntity = EntityFactory.newModelEntityWithId(0L);
        GroupsetEntity groupsetEntity = EntityFactory.newGroupsetEntityWithName("mock");

        // Given an entity made from those attributes
        BikeEntity bikeEntity = BikeEntity.builder()
                .id(id)
                .size(size)
                .heroImageUrl(heroImageUrl)
                .model(modelEntity)
                .groupset(groupsetEntity)
                .build();

        // Given an expected result of asBike
        Bike expectedBike = Bike.newBuilder()
                .id(id.toString())
                .size(size)
                .heroImageUrl(heroImageUrl)
                .model(modelEntity.asModel())
                .groupset(groupsetEntity.asGroupset())
                .build();

        // Then asBike should return a Bike object with the expected values
        assertThat(bikeEntity.asBike()).isEqualTo(expectedBike);
    }
}
