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

    public static ModelEntity newModelEntityWithId(Long id) {
        return ModelEntity.builder()
                .id(id)
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

    public static GroupsetEntity newGroupsetEntityWithName(String name) {
        return GroupsetEntity.builder()
                .brand(GroupsetBrand.SHIMANO)
                .isElectronic(false)
                .name(name)
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

    public static BikeEntity newBikeEntityWithId(Long id) {
        return BikeEntity.builder()
                .id(id)
                .size("54cm")
                .model(EntityFactory.newModelEntity())
                .groupset(EntityFactory.newGroupsetEntity())
                .build();
    }
}
