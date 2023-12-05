package servicecourse.repo.common;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.services.exceptions.EmptyStringFilterInputException;

import java.util.List;
import java.util.Optional;

public class StringFilterSpecification {
    /**
     * @param input     the details of the filter to apply to the field
     * @param fieldPath the path from the root entity (of type {@code T}) to the {@literal String}
     *                  attribute to apply the filter to
     * @param <T>       the entity for which {@code fieldPath} is an attribute
     * @return a specification ready to apply on the entity of type {@code T}
     * @throws EmptyStringFilterInputException if the input is empty
     */
    public static <T> Specification<T> from(@NonNull StringFilterInput input,
                                            SingularAttribute<T, String> fieldPath) throws EmptyStringFilterInputException {
        validate(input, fieldPath.getName());

        return (root, query, cb) -> {
            Expression<String> fieldExpression = root.get(fieldPath);

            return SpecificationCombiner.<T>or(List.of(
                    equalsSpecification(input.getEquals(), fieldExpression),
                    containsSpecification(input.getContains(), fieldExpression),
                    inSpecification(input.getIn(), fieldExpression)
            )).toPredicate(root, query, cb);
        };
    }

    private static void validate(StringFilterInput input, String fieldPath) {
        if (input.getContains() == null && input.getIn() == null && input.getEquals() == null) {
            throw new EmptyStringFilterInputException(fieldPath);
        }
    }

    private static <T> Optional<Specification<T>> equalsSpecification(String equals,
                                                                      Expression<String> fieldExpression) {
        return Optional.ofNullable(equals)
                .map(e -> ((root, query, cb) -> cb.equal(fieldExpression, equals)));
    }

    /** Case in-sensitive */
    private static <T> Optional<Specification<T>> containsSpecification(String contains,
                                                                        Expression<String> fieldExpression) {
        return Optional.ofNullable(contains)
                .map(c -> ((root, query, cb) -> cb.like(cb.lower(fieldExpression),
                                                        "%" + contains.toLowerCase() + "%")));
    }

    private static <T> Optional<Specification<T>> inSpecification(List<String> in,
                                                                  Expression<String> fieldExpression) {
        return Optional.ofNullable(in)
                .map(i -> ((root, query, cb) -> fieldExpression.in(in)));
    }
}
