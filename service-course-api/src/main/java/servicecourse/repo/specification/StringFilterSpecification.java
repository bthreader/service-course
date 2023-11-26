package servicecourse.repo.specification;

import servicecourse.generated.types.StringFilterInput;

import java.util.Optional;
import java.util.function.Predicate;

public class StringFilterSpecification {
    private StringFilterSpecification() { }

    public static Predicate<String> from(StringFilterInput input) {
        return arg -> Optional.ofNullable(input.getEquals()).map(arg::equals).orElse(true)
                || Optional.ofNullable(input.getContains()).map(arg::contains).orElse(true)
                || Optional.ofNullable(input.getIn()).map(in -> in.contains(arg)).orElse(true);
    }
}
