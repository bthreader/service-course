package servicecourse.repo;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import servicecourse.generated.types.GroupsetBrand;

@StaticMetamodel(GroupsetEntity.class)
public class GroupsetEntity_ {
    public static volatile SingularAttribute<GroupsetEntity, String> name;
    public static volatile SingularAttribute<GroupsetEntity, GroupsetBrand> brand;
    public static volatile SingularAttribute<GroupsetEntity, Boolean> isElectronic;
}
