package servicecourse.repo.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {
    public static <T> Specification<T> matchAll() {
        return (root, query, cb) -> alwaysTruePredicate(cb);
    }

    public static Predicate alwaysTruePredicate(CriteriaBuilder cb) {
        return cb.isTrue(cb.literal(true));
    }
}
