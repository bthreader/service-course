package servicecourse.repo;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.repo.common.SpecificationUtils;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.ArrayList;
import java.util.List;

public class BikeEntitySpecification {
    /**
     * Creates a criteria from a filter if specified, also forces fetch join on model and groupset
     * embedded entities for non-count based queries. If no filter is specified, or it's empty,
     * simply forces fetch join as previously described, will not apply any criteria to entities.
     *
     * @param input the details of the filter to apply to the entities, can be null
     * @return a specification equivalent to the filter input if non-null and non-empty, "match all"
     * otherwise. Forces fetch join for embedded entities regardless.
     */
    public static Specification<BikeEntity> from(@Nullable final BikesFilterInput input) {
        return (root, query, cb) -> {
            // Force fetch join on all queries apart from the count query used by JPA for paging
            if (query.getResultType() != Long.class) {
                root.fetch("model", JoinType.INNER);
                root.fetch("groupset", JoinType.INNER);
            }

            if (input == null) {
                return SpecificationUtils.alwaysTruePredicate(cb);
            }

            final List<Predicate> predicates = new ArrayList<>();

            // Bike entity predicate
            if (input.getSize() != null) {
                predicates.add(StringFilterSpecification.from(input.getSize(), BikeEntity_.size)
                                       .toPredicate(root, query, cb));
            }

            // Groupset entity predicate
            if (input.getGroupset() != null) {
                predicates.add(GroupsetEntitySpecification.from(input.getGroupset())
                                       .toPredicate(query.from(GroupsetEntity.class), query, cb));
            }

            // Model entity predicate
            if (input.getModel() != null) {
                predicates.add(ModelEntitySpecification.from(input.getModel())
                                       .toPredicate(query.from(ModelEntity.class), query, cb));
            }

            return predicates.isEmpty() ? SpecificationUtils.alwaysTruePredicate(cb)
                    : cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
