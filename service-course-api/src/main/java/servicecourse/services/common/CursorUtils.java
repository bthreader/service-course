package servicecourse.services.common;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import servicecourse.generated.types.Cursor;
import servicecourse.services.common.exceptions.InvalidCursorException;
import servicecourse.utils.Base64ToLongConverter;

import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class CursorUtils {
    /**
     * @param page         a sublist of entities
     * @param cursorGetter a function that takes an entity and obtains a cursor from it
     * @param <T>          the type of entity
     * @return a cursor based on the first element of the page, empty if page is empty
     */
    public static <T> Optional<Cursor> startCursor(Page<T> page, Function<T, String> cursorGetter) {
        return page.stream()
                .findFirst()
                .map(cursorGetter)
                .map(CursorUtils::cursorOf);
    }

    /**
     * @param page         a sublist of entities
     * @param cursorGetter a function that takes an entity and obtains a cursor from it
     * @param <T>          the type of entity
     * @return a cursor based on the last element of the page, empty if the page is empty
     */
    public static <T> Optional<Cursor> endCursor(Page<T> page, Function<T, String> cursorGetter) {
        if (page.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(page.stream()
                                   .toList()
                                   .get(page.getNumberOfElements() - 1))
                .map(cursorGetter)
                .map(CursorUtils::cursorOf);
    }

    /**
     * Simple wrapper around {@link Base64ToLongConverter} to translate
     * {@link IllegalArgumentException} into the more appropriate {@link InvalidCursorException}.
     *
     * @param cursorName the name of the cursor, makes exception message more useful
     * @param cursor     a Base64 encoded {@code Long}
     * @return the {@code Long}
     * @throws InvalidCursorException if {@code cursor} isn't a valid signed decimal long
     */
    public static Long decodeBase64CursorToLong(CursorName cursorName, String cursor) {
        try {
            return Base64ToLongConverter.decodeFromBase64(cursor);
        } catch (Exception e) {
            throw new InvalidCursorException(cursorName, cursor);
        }
    }

    private static Cursor cursorOf(String cursorValue) {
        return Cursor.newBuilder().cursor(cursorValue).build();
    }
}
