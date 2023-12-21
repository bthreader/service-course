package servicecourse.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Base64ToLongConverterTest {
    @Test
    void encode_and_decode() {
        assertThat(Base64ToLongConverter
                           .decodeFromBase64(
                                   Base64ToLongConverter
                                           .encodeToBase64(1L))).isEqualTo(1L);
    }

    @Test
    void decode_throws() {
        // Not valid
        assertThrows(IllegalArgumentException.class,
                     () -> Base64ToLongConverter.decodeFromBase64(Base64Factory.invalidBase64String()));

        // Valid but not a long
        assertThrows(IllegalArgumentException.class,
                     () -> Base64ToLongConverter.decodeFromBase64(Base64Factory.stringEncodedAsBase64String()));
    }
}
