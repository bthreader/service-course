package servicecourse.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class Base64ToLongConverter {
    public static String encodeToBase64(@NonNull Long value) {
        byte[] bytes = Long.toString(value).getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @param base64 a Base64 encoded {@code Long}
     * @return the {@code Long}
     * @throws IllegalArgumentException if {@code base64} isn't a Base64 encoded signed decimal
     *                                  long
     */
    public static Long decodeFromBase64(@NonNull String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return Long.parseLong(new String(bytes));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "'base46' is not a Base64 encoded signed decimal long: " + base64);
        }
    }
}
