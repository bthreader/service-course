package servicecourse.repo;

import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.BikesFilterInput;

public class BikeEntitySpecification {
    private BikeEntitySpecification() { }

    public static Specification<BikeEntity> from(BikesFilterInput input) {
        return (root, query, cb) -> GroupsetEntitySpecification.from(input.getGroupset())
                .toPredicate(query.from(GroupsetEntity.class), query, cb);
    }
}
