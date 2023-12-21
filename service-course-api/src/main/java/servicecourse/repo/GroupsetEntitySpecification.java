package servicecourse.repo;

import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.GroupsetBrand;
import servicecourse.generated.types.GroupsetFilterInput;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.repo.common.SpecificationUtils;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GroupsetEntitySpecification {
    /**
     * @param input the details of the filter to apply to the entities
     * @return a specification based on the input, if the input is empty the specification will be
     * equivalent to "match all"
     */
    public static Specification<GroupsetEntity> from(GroupsetFilterInput input) {
        List<Specification<GroupsetEntity>> specifications = Stream.of(
                        Optional.ofNullable(input.getBrand())
                                .map(GroupsetEntitySpecification::brand),
                        Optional.ofNullable(input.getName())
                                .map(GroupsetEntitySpecification::name),
                        Optional.ofNullable(input.getIsElectronic())
                                .map(GroupsetEntitySpecification::isElectronic))
                .flatMap(Optional::stream)
                .toList();

        return specifications.isEmpty() ? SpecificationUtils.matchAll()
                : Specification.allOf(specifications);
    }

    private static Specification<GroupsetEntity> isElectronic(boolean isElectronic) {
        return (root, query, cb) -> cb.equal(root.get(GroupsetEntity_.isElectronic), isElectronic);
    }

    private static Specification<GroupsetEntity> name(StringFilterInput input) {
        return (root, query, cb) -> StringFilterSpecification
                .from(input, GroupsetEntity_.name)
                .toPredicate(root, query, cb);
    }

    private static Specification<GroupsetEntity> brand(GroupsetBrand brand) {
        return (root, query, cb) -> cb.equal(root.get(GroupsetEntity_.brand), brand);
    }
}
