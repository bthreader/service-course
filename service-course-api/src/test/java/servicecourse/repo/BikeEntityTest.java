package servicecourse.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import servicecourse.generated.types.Bike;
import servicecourse.services.EntityFactory;
import servicecourse.services.bikes.UpdateBikeParams;
import servicecourse.utils.URLFactory;

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

    @Nested
    class apply {
        // The immutable base reference
        final BikeEntity baseBikeEntity = BikeEntity.builder()
                .id(0L)
                .size("medium")
                .heroImageUrl(null)
                .model(EntityFactory.newModelEntityWithId(0L))
                .groupset(EntityFactory.newGroupsetEntityWithName("old"))
                .build();
        // The mutable entity to compare with the base reference for side effects
        BikeEntity bikeEntity;

        @BeforeEach
        void setup() {
            // Reset the bike entity to the base
            bikeEntity = BikeEntity.builder()
                    .id(baseBikeEntity.getId())
                    .size(baseBikeEntity.getSize())
                    .heroImageUrl(baseBikeEntity.getHeroImageUrl())
                    .model(baseBikeEntity.getModel())
                    .groupset(baseBikeEntity.getGroupset())
                    .build();
        }

        @Test
        void hero_image_URL_update() {
            // Given a new URL
            URL newURL = URLFactory.newUrl().orElseThrow();

            // When the apply method is called with that URL
            bikeEntity.apply(UpdateBikeParams.builder()
                                     .heroImageUrl(newURL)
                                     .build());

            // Then the single update should have been applied
            assertThat(baseBikeEntity.getHeroImageUrl()).isNotEqualTo(newURL);
            assertThat(bikeEntity.getHeroImageUrl()).isEqualTo(newURL);

            // Then no other attributes should have been changed
            assertThat(bikeEntity.getId()).isEqualTo(baseBikeEntity.getId());
            assertThat(bikeEntity.getSize()).isEqualTo(baseBikeEntity.getSize());
            assertThat(bikeEntity.getModel()).isEqualTo(baseBikeEntity.getModel());
            assertThat(bikeEntity.getGroupset()).isEqualTo(baseBikeEntity.getGroupset());
        }

        @Test
        void groupset_update() {
            // Given a new groupset
            GroupsetEntity newGroupset = EntityFactory.newGroupsetEntityWithName("new!");

            // When the apply method is called with that URL
            bikeEntity.apply(UpdateBikeParams.builder()
                                     .groupset(newGroupset)
                                     .build());

            // Then a single update should have been applied
            assertThat(baseBikeEntity.getGroupset()).isNotEqualTo(newGroupset);
            assertThat(bikeEntity.getGroupset()).isEqualTo(newGroupset);

            // Then no other attributes should have been changed
            assertThat(bikeEntity.getId()).isEqualTo(baseBikeEntity.getId());
            assertThat(bikeEntity.getSize()).isEqualTo(baseBikeEntity.getSize());
            assertThat(bikeEntity.getModel()).isEqualTo(baseBikeEntity.getModel());
            assertThat(bikeEntity.getHeroImageUrl()).isEqualTo(baseBikeEntity.getHeroImageUrl());
        }

        @Test
        void no_update() {
            // When the apply method is called with an empty update
            bikeEntity.apply(UpdateBikeParams.builder().build());

            // There should have been no changes
            assertThat(bikeEntity).isEqualTo(baseBikeEntity);
        }
    }
}
