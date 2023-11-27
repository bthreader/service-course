package servicecourse.repo.common;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.services.Errors;

import java.util.List;
import java.util.Optional;

public class StringFilterSpecification {
    /**
     * Generates a specification from a StringFilterInput gql type given a fieldPath. Allowing for
     * filtering based on that field.
     */
    public static <T> Specification<T> from(StringFilterInput input,
                                            SingularAttribute<T, String> fieldPath) {
        validate(input);

        return (root, query, cb) -> {
            Expression<String> fieldExpression = root.get(fieldPath);

            return SpecificationCombiner.<T>or(List.of(
                    equalsSpecification(input.getEquals(), fieldExpression),
                    containsSpecification(input.getContains(), fieldExpression),
                    inSpecification(input.getIn(), fieldExpression)
            )).toPredicate(root, query, cb);
        };
    }

    private static void validate(StringFilterInput input) {
        if (input.getContains() == null && input.getIn() == null && input.getEquals() == null) {
            throw Errors.emptyStringFilterInput();
        }
    }

    private static <T> Optional<Specification<T>> equalsSpecification(String equals,
                                                                      Expression<String> fieldExpression) {
        return Optional.ofNullable(equals)
                .map(e -> ((root, query, cb) -> cb.equal(fieldExpression, equals)));
    }

    private static <T> Optional<Specification<T>> containsSpecification(String contains,
                                                                        Expression<String> fieldExpression) {
        return Optional.ofNullable(contains)
                .map(c -> ((root, query, cb) -> cb.like(fieldExpression, "%" + contains + "%")));
    }

    private static <T> Optional<Specification<T>> inSpecification(List<String> in,
                                                                  Expression<String> fieldExpression) {
        return Optional.ofNullable(in)
                .map(i -> ((root, query, cb) -> fieldExpression.in(in)));
    }
}
