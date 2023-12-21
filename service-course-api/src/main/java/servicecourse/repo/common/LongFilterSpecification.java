package servicecourse.repo.common;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public class LongFilterSpecification {
    public static <T> Specification<T> newGreaterThanSpecification(Long greaterThan,
                                                                   SingularAttribute<T, Long> fieldPath) {
        return (root, query, cb) -> {
            Expression<Long> fieldExpression = root.get(fieldPath);
            return cb.greaterThan(fieldExpression, greaterThan);
        };
    }
}
