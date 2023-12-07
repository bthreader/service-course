package servicecourse.repo.common;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.StringFilterInput;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringFilterSpecification {
    /**
     * @param input     the details of the filter to apply to the field
     * @param fieldPath the path from the root entity (of type {@code T}) to the {@code String}
     *                  attribute to apply the filter on
     * @param <T>       the entity for which {@code fieldPath} is an attribute
     * @return a specification ready to apply to entities of type {@code T}, if the input is empty
     * the specification will be equivalent to "match all"
     */
    public static <T> Specification<T> from(@NonNull StringFilterInput input,
                                            SingularAttribute<T, String> fieldPath) {
        return (root, query, cb) -> {
            Expression<String> fieldExpression = root.get(fieldPath);

            List<Specification<T>> specifications = Stream.<Optional<Specification<T>>>of(
                            equalsSpecification(input.getEquals(), fieldExpression),
                            containsSpecification(input.getContains(), fieldExpression),
                            inSpecification(input.getIn(), fieldExpression))
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());

            return specifications.isEmpty() ? SpecificationUtils.alwaysTruePredicate(cb)
                    : Specification.anyOf(specifications).toPredicate(root, query, cb);
        };
    }

    private static <T> Optional<Specification<T>> equalsSpecification(String equals,
                                                                      Expression<String> fieldExpression) {
        return Optional.ofNullable(equals)
                .map(e -> ((root, query, cb) -> cb.equal(fieldExpression, equals)));
    }

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
