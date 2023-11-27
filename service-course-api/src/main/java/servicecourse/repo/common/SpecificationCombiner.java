package servicecourse.repo.common;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class SpecificationCombiner {
    /**
     * If all specifications are empty, a null predicate will be returned. Therefore, clients are
     * encouraged to ensure that at least one specification is guaranteed to not be null at
     * runtime.
     */
    public static <T> Specification<T> and(List<Optional<Specification<T>>> specifications) {
        return specifications.stream()
                .flatMap(Optional::stream)
                .reduce(Specification.where(null), Specification::and);
    }

    /**
     * If all specifications are empty, a null predicate will be returned. Therefore, clients are
     * encouraged to ensure that at least one specification is guaranteed to not be null at
     * runtime.
     */
    public static <T> Specification<T> or(List<Optional<Specification<T>>> specifications) {
        return specifications.stream()
                .flatMap(Optional::stream)
                .reduce(Specification.where(null), Specification::or);
    }
}
