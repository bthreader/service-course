package servicecourse.repo;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(GroupsetEntity.class)
public class ModelEntity_ {
    public static volatile SingularAttribute<ModelEntity, Long> id;
    public static volatile SingularAttribute<ModelEntity, String> name;
    public static volatile SingularAttribute<ModelEntity, Integer> modelYear;
    public static volatile SingularAttribute<ModelEntity, String> brandName;
}
