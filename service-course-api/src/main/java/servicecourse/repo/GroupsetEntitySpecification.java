package servicecourse.repo;

import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.GroupsetBrand;
import servicecourse.generated.types.GroupsetFilterInput;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.repo.common.SpecificationCombiner;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.List;
import java.util.Optional;

public class GroupsetEntitySpecification {
    private GroupsetEntitySpecification() { }

    public static Specification<GroupsetEntity> from(GroupsetFilterInput input) {
        return SpecificationCombiner.and(List.of(Optional.ofNullable(input.getBrand())
                                                         .map(GroupsetEntitySpecification::brand),
                                                 Optional.ofNullable(input.getName())
                                                         .map(GroupsetEntitySpecification::name),
                                                 Optional.ofNullable(
                                                                 input.getIsElectronic())
                                                         .map(GroupsetEntitySpecification::isElectronic)));
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
