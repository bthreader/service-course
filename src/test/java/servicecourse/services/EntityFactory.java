package servicecourse.services;

import servicecourse.generated.types.GroupsetBrand;
import servicecourse.repo.BikeEntity;
import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.ModelEntity;

public class EntityFactory {
    public static ModelEntity newModelEntity() {
        return ModelEntity.builder()
                .id(1L)
                .name("Allez Sprint")
                .brandName("Specialized")
                .modelYear(2023)
                .build();
    }

    public static GroupsetEntity newGroupsetEntity() {
        return GroupsetEntity.builder()
                .brand(GroupsetBrand.SHIMANO)
                .isElectronic(false)
                .name("105 R7000 11s")
                .build();
    }

    public static BikeEntity newBikeEntity() {
        return BikeEntity.builder()
                .id(1L)
                .size("54cm")
                .model(EntityFactory.newModelEntity())
                .groupset(EntityFactory.newGroupsetEntity())
                .build();
    }
}
