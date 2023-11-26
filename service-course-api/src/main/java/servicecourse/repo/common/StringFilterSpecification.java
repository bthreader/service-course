package servicecourse.repo.common;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.StringFilterInput;

import java.util.Optional;

public class StringFilterSpecification<T> {
    /**
     * Generates a specification from a StringFilterInput gql type given a fieldPath. Allowing for
     * filtering based on that field in the persistence.
     */
    public Specification<T> from(StringFilterInput input, SingularAttribute<T, String> fieldPath) {
        return (root, query, cb) -> {
            Expression<String> fieldExpression = root.get(fieldPath);
            Predicate alwaysTrue = cb.isTrue(cb.literal(true));

            return cb.and(Optional.ofNullable(input.getEquals())
                                  .map(eq -> cb.equal(fieldExpression, eq))
                                  .orElse(alwaysTrue),
                          Optional.ofNullable(input.getContains())
                                  .map(contains -> cb.like(fieldExpression, "%" + contains + "%"))
                                  .orElse(alwaysTrue),
                          Optional.ofNullable(input.getIn())
                                  .map(fieldExpression::in)
                                  .orElse(alwaysTrue)
            );
        };
    }
}
