package servicecourse.repo;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.repo.common.SpecificationUtils;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.ArrayList;
import java.util.List;

public class BikeEntitySpecification {
    /**
     * @param input the details of the filter to apply to the entities
     * @return a specification based on the input, if the input is empty the specification will be
     * equivalent to "match all"
     * @throws NullPointerException if input is null
     */
    public static Specification<BikeEntity> from(@NonNull BikesFilterInput input) {
        return (root, query, cb) -> {
            // Force fetch join
            root.fetch("model", JoinType.INNER);
            root.fetch("groupset", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

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
