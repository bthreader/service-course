package servicecourse.repo.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utilities to help create JPA specifications.
 */
@UtilityClass
public class SpecificationUtils {
    /**
     * @param <T> the type of the entity which the specification concerns
     * @return a specification equivalent to "match all"
     */
    public static <T> Specification<T> matchAll() {
        return (root, query, cb) -> alwaysTruePredicate(cb);
    }

    /**
     * @param cb the criteria builder being used to construct a query
     * @return a predicate which will always be true
     */
    public static Predicate alwaysTruePredicate(CriteriaBuilder cb) {
        return cb.isTrue(cb.literal(true));
    }
}
