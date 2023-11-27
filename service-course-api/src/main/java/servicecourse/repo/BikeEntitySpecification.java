package servicecourse.repo;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.BikesFilterInput;

public class BikeEntitySpecification {
    private BikeEntitySpecification() { }

    public static Specification<BikeEntity> from(BikesFilterInput input) {
        return (root, query, cb) -> {
            root.fetch("model", JoinType.INNER);
            root.fetch("groupset", JoinType.INNER);

            return GroupsetEntitySpecification.from(input.getGroupset())
                    .toPredicate(query.from(GroupsetEntity.class), query, cb);
        };
    }
}
