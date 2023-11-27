package servicecourse.repo;

import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.ModelFilterInput;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.repo.common.SpecificationCombiner;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.List;
import java.util.Optional;

public class ModelEntitySpecification {
    private ModelEntitySpecification() { }

    public static Specification<ModelEntity> from(ModelFilterInput input) {
        return SpecificationCombiner.and(List.of(Optional.ofNullable(input.getName())
                                                         .map(ModelEntitySpecification::name)));
    }

    private static Specification<ModelEntity> name(StringFilterInput input) {
        return (root, query, cb) -> StringFilterSpecification
                .from(input, ModelEntity_.name)
                .toPredicate(root, query, cb);
    }
}
