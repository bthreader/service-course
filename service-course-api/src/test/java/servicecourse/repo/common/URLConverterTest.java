package servicecourse.repo.common;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class URLConverterTest {
    private final URLConverter urlConverter = new URLConverter();
    private static URL mockUrl;
    private static final String mockUrlString = "https://test.com";

    @BeforeAll
    static void setup() {
        try {
            mockUrl = new URL(mockUrlString);
        } catch (MalformedURLException e) {
            fail();
        }
    }

    @Nested
    class convertToDatabaseColumn {
        @Test
        void handles_URL() {
            assertThat(urlConverter.convertToDatabaseColumn(mockUrl))
                    .isEqualTo(mockUrlString);
        }

        @Test
        void handles_null() {
            assertThat(urlConverter.convertToDatabaseColumn(null))
                    .isNull();
        }
    }

    @Nested
    class convertToEntityAttribute {
        @Test
        void handles_valid_string() {
            assertThat(urlConverter.convertToEntityAttribute(mockUrlString))
                    .isEqualTo(mockUrl);
        }

        @Test
        void handles_invalid_string() {
            assertThrows(IllegalArgumentException.class,
                         () -> urlConverter.convertToEntityAttribute("not_a_url"));
        }

        @Test
        void handles_null() {
            assertThat(urlConverter.convertToEntityAttribute(null))
                    .isNull();
        }
    }
}
