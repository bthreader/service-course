package servicecourse.repo;

import org.junit.jupiter.api.Test;
import servicecourse.generated.types.Groupset;
import servicecourse.generated.types.GroupsetBrand;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupsetEntityTest {
    @Test
    void asGroupset() {
        // Given some attributes
        String name = "name";
        GroupsetBrand brand = GroupsetBrand.SHIMANO;
        boolean isElectronic = false;

        // Given an entity with those attributes
        GroupsetEntity entity = GroupsetEntity.builder()
                .name(name)
                .brand(brand)
                .isElectronic(isElectronic)
                .build();

        // Given the equivalent model
        Groupset groupset = Groupset.newBuilder()
                .name(name)
                .brand(brand)
                .isElectronic(isElectronic)
                .build();

        // Then the method applied on the entity should return the groupset equivalent
        assertThat(entity.asGroupset()).isEqualTo(groupset);
    }
}
