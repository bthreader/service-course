package servicecourse.repo;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(BikeEntity.class)
public class BikeEntity_ {
    public static volatile SingularAttribute<BikeEntity, String> size;
}
