package servicecourse.repo;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import servicecourse.generated.types.GroupsetBrand;
import servicecourse.generated.types.GroupsetFilterInput;
import servicecourse.generated.types.StringFilterInput;
import servicecourse.repo.common.StringFilterSpecification;

import java.util.Optional;

public class GroupsetEntitySpecification {
    private static final StringFilterSpecification<GroupsetEntity> stringFilterSpecification = new StringFilterSpecification<>();

    private GroupsetEntitySpecification() { }

    public static Specification<GroupsetEntity> from(GroupsetFilterInput input) {
        return (root, query, cb) -> {
            Predicate alwaysTrue = cb.isTrue(cb.literal(true));

            return cb.and(
                    Optional.ofNullable(input.getIsElectronic())
                            .map(isElectronic -> isElectronic(isElectronic).toPredicate(root,
                                                                                        query,
                                                                                        cb))
                            .orElse(alwaysTrue),
                    Optional.ofNullable(input.getName())
                            .map(stringFilter -> stringFilterSpecification.from(stringFilter,
                                                                                GroupsetEntity_.name)
                                    .toPredicate(root, query, cb))
                            .orElse(alwaysTrue),
                    Optional.ofNullable(input.getBrand())
                            .map(brand -> brand(brand).toPredicate(root, query, cb))
                            .orElse(alwaysTrue)
            );
        };
    }

    private static Specification<GroupsetEntity> isElectronic(boolean isElectronic) {
        return (root, query, cb) -> cb.equal(root.get(GroupsetEntity_.isElectronic), isElectronic);
    }

    private static Specification<GroupsetEntity> name(StringFilterInput input) {
        return (root, query, cb) -> stringFilterSpecification
                .from(input, GroupsetEntity_.name)
                .toPredicate(root, query, cb);
    }

    private static Specification<GroupsetEntity> brand(GroupsetBrand brand) {
        return (root, query, cb) -> cb.equal(root.get(GroupsetEntity_.brand), brand);
    }
}
