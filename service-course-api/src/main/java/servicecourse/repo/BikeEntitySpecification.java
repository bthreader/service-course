package servicecourse.repo;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.repo.common.StringFilterSpecification;

public class BikeEntitySpecification {
    private BikeEntitySpecification() { }

    public static Specification<BikeEntity> from(BikesFilterInput input) {
        return (root, query, cb) -> {
            // Force fetch join
            root.fetch("model", JoinType.INNER);
            root.fetch("groupset", JoinType.INNER);

            // TODO these are NOT null safe
            Predicate bikeEntityPredicate = StringFilterSpecification.from(input.getSize(),
                                                                           BikeEntity_.size)
                    .toPredicate(root, query, cb);

            // Apply filters on the subfields
            Predicate groupsetEntityPredicate = GroupsetEntitySpecification.from(
                    input.getGroupset()).toPredicate(query.from(GroupsetEntity.class), query, cb);
            Predicate modelEntityPredicate = ModelEntitySpecification.from(
                    input.getModel()).toPredicate(query.from(ModelEntity.class), query, cb);

            return cb.and(bikeEntityPredicate, groupsetEntityPredicate, modelEntityPredicate);
        };
    }
}
