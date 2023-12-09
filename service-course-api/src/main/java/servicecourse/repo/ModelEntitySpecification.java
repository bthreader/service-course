package servicecourse.repo;

import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.ModelFilterInput;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.repo.common.SpecificationUtils;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelEntitySpecification {
    /**
     * @param input the details of the filter to apply to the entities
     * @return a specification based on the input, if the input is empty the specification will be
     * equivalent to "match all"
     */
    public static Specification<ModelEntity> from(ModelFilterInput input) {
        List<Specification<ModelEntity>> specifications = Stream.of(
                        Optional.ofNullable(input.getName())
                                .map(ModelEntitySpecification::name))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        return specifications.isEmpty() ? SpecificationUtils.matchAll()
                : Specification.allOf(specifications);
    }

    private static Specification<ModelEntity> name(StringFilterInput input) {
        return (root, query, cb) -> StringFilterSpecification
                .from(input, ModelEntity_.name)
                .toPredicate(root, query, cb);
    }
}
