package servicecourse.repo.common;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SortUtils {
    public static Sort sortByIdAsc() {
        return Sort.by("id").ascending();
    }
}
