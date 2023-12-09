package servicecourse.repo.common;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.IntegerFilterInput;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntegerFilterSpecification {
    /**
     * @param input     the details of the integer filter to apply to field
     * @param fieldPath the path from the root entity (of type {@code T}) to the {@code Integer}
     *                  attribute to apply the filter on
     * @param <T>       the entity for which {@code fieldPath} is an attribute
     * @return a specification ready to apply to entities of type {@code T}, if the input is empty
     * the specification will be equivalent to "match all"
     * @throws NullPointerException if {@code input} is null
     */
    public static <T> Specification<T> from(@NonNull IntegerFilterInput input,
                                            SingularAttribute<T, Integer> fieldPath) {
        return (root, query, cb) -> {
            Expression<Integer> fieldExpression = root.get(fieldPath);

            List<Specification<T>> specifications = Stream.<Optional<Specification<T>>>of(
                            equalsSpecification(input.getEquals(), fieldExpression),
                            inSpecification(input.getIn(), fieldExpression))
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());

            return specifications.isEmpty() ? SpecificationUtils.alwaysTruePredicate(cb)
                    : Specification.anyOf(specifications).toPredicate(root, query, cb);
        };
    }

    private static <T> Optional<Specification<T>> equalsSpecification(Integer equals,
                                                                      Expression<Integer> fieldExpression) {
        return Optional.ofNullable(equals)
                .map(e -> ((root, query, cb) -> cb.equal(fieldExpression, equals)));
    }

    private static <T> Optional<Specification<T>> inSpecification(List<Integer> in,
                                                                  Expression<Integer> fieldExpression) {
        return Optional.ofNullable(in)
                .map(i -> ((root, query, cb) -> fieldExpression.in(in)));
    }
}
